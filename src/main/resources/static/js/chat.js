'use strict';
$(window).on("load", function () {
    let messageInput;
    let messageArea;
    let stompClient;
    let username;
    let id;
    let date;
    let login;
    let connectingError;
    let chatWindow;
    let currentEditMessageId;
    messageInput = document.querySelector('#message');
    messageArea = document.querySelector('#messageArea');
    stompClient = null;
    username = null;
    id = null;
    date = new Date();
    login = null;
    let cancelEdit = document.getElementById('cancelEdit');
    let editButton = document.getElementById('edit');
    let sendButton = document.getElementById('send');
    let editChatTitleButton = document.getElementById('editChatTitle');
    let confirmEditChatTitle = document.getElementById('confirmEditChatTitle');
    let editChatTitleInput = document.getElementById('editChatTitleInput');
    let chatTitle = document.getElementById('chatTitle');
    let roomId = document.querySelector('#roomId');
    connectingError = document.createElement('div');
    chatWindow = $("#chatWindow");
    currentEditMessageId = null;
    let disabled = false;
    let disableMessaging = false;
    let isSearching = document.getElementById('isSearching').innerText;

    connectingError.classList.add('hidden', 'text-danger', 'text-center');
    connectingError.id = 'connectionError';
    if (!stompClient)
        connect();
    chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
    if (messageInput != null)
        messageInput.focus();


    function connect() {
        id = document.querySelector('#id').textContent.trim();
        username = document.querySelector('#username').innerText.trim();
        login = document.querySelector('#login').textContent.trim();
        if (username) {
            let socket = new SockJS('http://' + window.location.host + '/ws');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, onConnected, onError);
            stompClient.debug = true;
        }
    }


    $('#messageSendForm').bind("keypress", function (e) {
        if (e.keyCode === 13 && currentEditMessageId === null && disabled === false) {
            sendMessage(e);
            disableSendButton();
            setTimeout(enableSendButton, 3000);
        } else if (e.keyCode === 13 && currentEditMessageId !== null && disabled === false) {
            sendEditedMessage(e);
            disableSendButton();
            setTimeout(enableSendButton, 3000);
        } else if (e.keyCode === 13)
            e.preventDefault();
    });


    function enableSendButton() {
        disabled = false;
    }


    function disableSendButton() {
        disabled = true;
    }


    function isUrl(s) {
        let regexp = /^https?:\/\/(www\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\.[a-z]{2,4}\b([-a-zA-Z0-9@:%_+.~#?&/=]*)/;
        return regexp.test(s);
    }


    async function onConnected() {
        stompClient.subscribe('/topic/chat/' + roomId.textContent, onMessageReceived);
        stompClient.subscribe('/topic/onlineTracker', onStatusChange);
        stompClient.subscribe('/topic/editChatTitle', onTitleChange);
        stompClient.subscribe('/topic/removeRoomMember', onRemoveRoomMember);
    }


    function onError(error) {
        console.log(error);
        connectingError.style.visibility = "visible";
        connectingError.textContent = 'Невозможно подключится к серверу, попробуйте позже';
        messageArea.appendChild(connectingError);
    }


    function onStatusChange(payload) {
        let statusChangeMessage = JSON.parse(payload.body);
        let checkOffline = document.getElementById('offline' + statusChangeMessage.id);
        let checkOnline = document.getElementById('online' + statusChangeMessage.id);
        if (statusChangeMessage.status === 'ONLINE' && id !== statusChangeMessage.id) {
            let parent = document.getElementById('onlineUsers');
            let a = document.createElement('a');
            let div1 = document.createElement('div');
            let div2 = document.createElement('div');
            let img = document.createElement('img');
            let div3 = document.createElement('div');
            let div4 = document.createElement('div');
            let div5 = document.createElement('div');
            let span = document.createElement('span');
            let offlineElement = document.getElementById('offline' + statusChangeMessage.id);
            let offlineParent = document.getElementById('offlineUsers');
            if (!checkOnline && checkOffline) {
                a.setAttribute('href', "/user/" + statusChangeMessage.id);
                div1.classList.add('row', 'userStatus');
                div1.style.padding = '5px';
                div1.style.paddingLeft = '10px';
                div1.id = 'online' + statusChangeMessage.id;
                div2.style.padding = '0px';
                div2.classList.add('col-4', 'col-sm-7', 'col-md-5', 'col-lg-3', 'col-xl-3', 'offset-xl-0', 'd-sm-flex', 'd-md-flex', 'd-lg-flex', 'justify-content-sm-center', 'align-items-sm-center', 'justify-content-md-center', 'align-items-md-center', 'justify-content-lg-center', 'align-items-lg-center');
                img.classList.add('rounded-circle', 'd-lg-flex', 'justify-content-lg-center', 'align-items-lg-center');
                img.src = '/media/avatars/avatar' + statusChangeMessage.id + '.png';
                img.style.width = '25px';
                img.style.height = '25px';
                div2.appendChild(img);
                div1.appendChild(div2);
                div3.classList.add('col-8', 'col-sm-12', 'col-md-7', 'col-xl-7', 'offset-xl-0', 'd-xl-flex', 'justify-content-xl-start', 'align-items-xl-center');
                div3.style.paddingLeft = '5px';
                div4.classList.add('row');
                div4.style.margin = '0px';
                div5.classList.add('col-xl-12', 'offset-xl-0', 'your_chats_online_users');
                div5.style.paddingLeft = '0px';
                div5.style.paddingRight = '0px';
                span.style.fontSize = '13px';
                span.innerText = statusChangeMessage.username;
                div5.appendChild(span);
                div4.appendChild(div5);
                div3.appendChild(div4);
                div1.appendChild(div3);
                a.appendChild(div1);
                a.id = 'online' + statusChangeMessage.id;
                parent.appendChild(a);
                if (offlineElement)
                    offlineParent.removeChild(offlineElement);
            }
        } else if (statusChangeMessage.status === 'OFFLINE' && id !== statusChangeMessage.id) {
            let parent = document.getElementById('offlineUsers');
            let a = document.createElement('a');
            let div1 = document.createElement('div');
            let div2 = document.createElement('div');
            let img = document.createElement('img');
            let div3 = document.createElement('div');
            let div4 = document.createElement('div');
            let div5 = document.createElement('div');
            let span = document.createElement('span');
            let onlineElement = document.getElementById('online' + statusChangeMessage.id);
            let onlineParent = document.getElementById('onlineUsers');
            if (!checkOffline && checkOnline) {
                a.setAttribute('href', "/user/" + statusChangeMessage.id);
                div1.classList.add('row', 'userStatus');
                div1.style.padding = '5px';
                div1.style.paddingLeft = '10px';
                div1.id = 'offline' + statusChangeMessage.id;
                div2.style.padding = '0px';
                div2.classList.add('col-4', 'col-sm-7', 'col-md-5', 'col-lg-3', 'col-xl-3', 'offset-xl-0', 'd-sm-flex', 'd-md-flex', 'd-lg-flex', 'justify-content-sm-center', 'align-items-sm-center', 'justify-content-md-center', 'align-items-md-center', 'justify-content-lg-center', 'align-items-lg-center');
                img.classList.add('rounded-circle', 'd-lg-flex', 'justify-content-lg-center', 'align-items-lg-center');
                img.src = '/media/avatars/avatar' + statusChangeMessage.id + '.png';
                img.style.width = '25px';
                img.style.height = '25px';
                div2.appendChild(img);
                div1.appendChild(div2);
                div3.classList.add('col-8', 'col-sm-12', 'col-md-7', 'col-xl-7', 'offset-xl-0', 'd-xl-flex', 'justify-content-xl-start', 'align-items-xl-center');
                div3.style.paddingLeft = '5px';
                div4.classList.add('row');
                div4.style.margin = '0px';
                div5.classList.add('col-xl-12', 'offset-xl-0', 'your_chats_online_users');
                div5.style.paddingLeft = '0px';
                div5.style.paddingRight = '0px';
                span.style.fontSize = '13px';
                span.innerText = statusChangeMessage.username;
                div5.appendChild(span);
                div4.appendChild(div5);
                div3.appendChild(div4);
                div1.appendChild(div3);
                a.appendChild(div1);
                a.id = 'offline' + statusChangeMessage.id;
                parent.appendChild(a);
                if (onlineElement)
                    onlineParent.removeChild(onlineElement);
            }
        }
    }


    function sendMessage(event) {
        event.preventDefault();
        let messageContent = messageInput.value.trim();
        if (messageContent.length > 1024) {
            let notificationDiv = document.createElement('div');
            notificationDiv.classList.add('top-left', 'notify', 'do-show');
            notificationDiv.style.backgroundColor = '#66BB6A';
            let notificationSpan = document.createElement('span');
            notificationSpan.innerText = 'Сообщение слишком большое!';
            notificationDiv.appendChild(notificationSpan);
            document.body.appendChild(notificationDiv);
            messageInput.value = '';
        } else {
            if (messageContent && stompClient) {
                let chatMessage = {
                    roomId: roomId.textContent,
                    messageId: '',
                    id: id,
                    sender: username,
                    login: login,
                    content: messageInput.value,
                    type: 'CHAT',
                    timestamp: date
                };
                stompClient.send("/app/chat.saveAndSendMessage." + roomId.textContent, {}, JSON.stringify(chatMessage));
                messageInput.value = '';
            }
            let file = document.getElementById('file');
            if (file.files.length !== 0 && stompClient) {
                let form_data = new FormData();
                let file_data = file.files;
                for (let i = 0; i < file_data.length; i++) {
                    form_data.append('file' + i, file_data[i]);
                }
                form_data.append('id', $('#id').text());
                form_data.append('timestamp', new Date().getTime().toString());
                form_data.append('roomId', roomId.textContent);
                form_data.append('sender', username);
                let token = $("meta[name='_csrf']").attr("content");
                let ajaxReq = $.ajax({
                    url: "/chat/" + roomId.textContent,
                    type: "POST",
                    headers: {"X-CSRF-TOKEN": token},
                    data: form_data,
                    enctype: 'multipart/form-data',
                    processData: false,
                    contentType: false,
                    cache: false,
                    success: function () {
                        let o = document.getElementById("progress");
                        let fileButton = document.getElementById('fileLabel');
                        let cancelEditButton = document.getElementById('cancelEdit');
                        let fileDrag = document.getElementById('filedragDIV');
                        fileDrag.style.display = 'none';
                        o.innerHTML = '';
                        fileButton.style.display = 'inline-block';
                        cancelEditButton.style.display = 'none';
                    }
                });
                file.value = '';
            }
        }
    }


    function FileSelectHandler(e) {
        e.preventDefault();
        e.stopPropagation();
        let fileButton = document.getElementById('fileLabel');
        let cancelEditButton = document.getElementById('cancelEdit');
        let fileInput = document.getElementById('file');
        cancelEditButton.style.display = 'inline-block';
        fileButton.style.display = 'none';
        let files = e.target.files || e.dataTransfer.files;
        fileInput.files = files;
        Array.from(files).forEach(f =>
            UploadFile(f)
        );
    }


    function UploadFile(file) {
        let xhr = new XMLHttpRequest();
        let fileDrag = document.getElementById('filedragDIV');
        fileDrag.style.display = 'block';
        if (xhr.upload) {
            let o = document.getElementById("progress");
            let progress = o.appendChild(document.createElement("p"));
            progress.appendChild(document.createTextNode(file.name));
            xhr.upload.addEventListener("progress", function (e) {
                let pc = parseInt(100 - (e.loaded / e.total * 100));
                progress.style.backgroundPosition = pc + "% 0";
            }, false);
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    progress.className = (xhr.status === 200 ? "success" : "failure");
                }
            };
            let token = $("meta[name='_csrf']").attr("content");
            let boundary = String(Math.random()).slice(2);
            xhr.open("POST", "/chat/" + roomId.textContent, true);
            xhr.setRequestHeader("X-CSRF-TOKEN", token);
            xhr.setRequestHeader('Content-Type', 'multipart/form-data;boundary=' + boundary);
            xhr.send(file);
        }

    }


    let dragging = 0;


    $(document).on('dragover', function (e) {
        let fileDragDIV = document.getElementById("filedragDIV");
        fileDragDIV.style.display = 'block';
        e.stopPropagation();
        e.preventDefault();
    });


    $('#filedrag').on('dragover', function (e) {
        let fileDrag = document.getElementById('filedrag');
        fileDrag.classList.add('hover');
        e.stopPropagation();
        e.preventDefault();
    });


    $('#filedrag').on('dragleave', function (e) {
        let fileDrag = document.getElementById('filedrag');
        fileDrag.classList.remove('hover');
        fileDrag.classList.add('col-9', 'col-lg-10');
        e.stopPropagation();
        e.preventDefault();
    });


    $(document).on('dragenter', function (e) {
        let fileDragDIV = document.getElementById("filedragDIV");
        dragging++;
        fileDragDIV.style.display = 'block';
        e.stopPropagation();
        e.preventDefault();
    });


    $(document).on('dragleave', function (e) {
        dragging--;
        let fileDrag = document.getElementById("filedragDIV");
        if (dragging === 0)
            fileDrag.style.display = 'none';
        e.stopPropagation();
        e.preventDefault();
    });


    function Init() {
        let fileSelect = document.getElementById("file"),
            fileDrag = document.getElementById("filedrag");
        if (fileSelect != null) {
            fileSelect.addEventListener("change", FileSelectHandler, false);
            let xhr = new XMLHttpRequest();
            if (xhr.upload) {
                fileDrag.addEventListener("drop", FileSelectHandler, false);
                fileDrag.style.display = "block";
            }
        }
    }


    if (window.File && window.FileList && window.FileReader) {
        Init();
    }


    $("audio").each(function () {
        $(this).bind("play", stopAll);
    });


    function stopAll(e) {
        let currentElementId = $(e.currentTarget).attr("id");
        $("audio").each(function () {
            let $this = $(this);
            let elementId = $this.attr("id");
            if (elementId !== currentElementId) {
                $this[0].pause();
            }
        });
    }


    function onMessageReceived(payload) {
        if (!disableMessaging && isSearching === 'false') {
            let receivedMessage = JSON.parse(payload.body);
            if ((receivedMessage.type === 'CHAT' || receivedMessage.type === 'IMAGE' || receivedMessage.type === 'AUDIO' || receivedMessage.type === 'VIDEO') && (receivedMessage.id !== id && receivedMessage.roomId === roomId.textContent)) {
                let audio = new Audio('/media/notification.mp3');
                audio.play();
            }
            if (receivedMessage.roomId === roomId.textContent) {
                if (receivedMessage.content == null)
                    return;
                let messagePreview = document.getElementById('messagePreview' + roomId.textContent);
                let messagePreviewSpan = document.createElement('span');
                let messagePreviewImg = document.getElementById('imgPreview' + roomId.textContent);
                messagePreviewImg.src = '/media/avatars/avatar' + receivedMessage.id + '.png';
                let usernameDiv = document.createElement('div');
                let usernameSpan = document.createElement('span');
                let usernameHref = document.createElement('a');
                usernameDiv.classList.add('row', 'd-flex');
                usernameDiv.style.fontWeight = '550';
                usernameSpan.innerText = receivedMessage.sender;
                usernameHref.href = '/user/' + receivedMessage.id;
                usernameHref.draggable = false;
                usernameHref.appendChild(usernameSpan);
                usernameDiv.appendChild(usernameHref);
                if (receivedMessage.type === 'CHAT') {
                    messagePreviewSpan.innerText = receivedMessage.sender + ': ' + receivedMessage.content;
                    while (messagePreview.firstChild)
                        messagePreview.firstChild.remove();
                    messagePreview.appendChild(messagePreviewSpan);
                    let date = new Date(receivedMessage.timestamp);
                    let div1 = document.createElement('div');
                    let div2 = document.createElement('div');
                    let div3 = document.createElement('div');
                    let div4 = document.createElement('div');
                    let div5 = document.createElement('div');
                    let div6 = document.createElement('div');
                    let div7 = document.createElement('div');
                    let div8 = document.createElement('div');
                    let dateElement = document.createElement('span');
                    let messageLink = document.createElement('a');
                    let authorImg = document.createElement('img');
                    let messageContent;
                    div1.classList.add('row');
                    div1.style.padding = '10px';
                    if (id !== receivedMessage.id) {
                        div1.classList.add('row', 'opponents_message', 'text');
                        div1.id = 'message' + receivedMessage.messageId;
                        div2.classList.add('col-4', 'col-sm-2', 'col-lg-2', 'col-xl-1', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-center', 'align-items-sm-start');
                        messageLink.setAttribute('href', "/user/" + receivedMessage.id);
                        authorImg.classList.add('rounded-circle', 'image--cover');
                        authorImg.src = '/media/avatars/avatar' + receivedMessage.id + '.png';
                        authorImg.style.width = '50px';
                        authorImg.style.height = '50px';
                        messageLink.appendChild(authorImg);
                        div2.appendChild(messageLink);
                        div3.classList.add('col-sm-7', 'col-xl-4');
                        div3.appendChild(usernameDiv);
                        div4.classList.add('row');
                        div5.classList.add('col-sm-12', 'col-xl-11', 'offset-xl-0');
                        div5.style.padding = '5px';
                        div6.classList.add('d-sm-flex', 'd-md-flex', 'd-lg-flex', 'd-xl-flex', 'justify-content-sm-start', 'justify-content-md-start', 'justify-content-lg-start', 'justify-content-xl-start', 'align-items-xl-center');
                        if (isUrl(receivedMessage.content)) {
                            messageContent = document.createElement('a');
                            messageContent.classList.add('message');
                            messageContent.style.marginLeft = '5px';
                            messageContent.style.fontSize = '14px';
                            messageContent.href = receivedMessage.content;
                            messageContent.innerText = receivedMessage.content;
                            messageContent.target = '_blank';
                            messageContent.rel = 'noopener noreferrer';
                            messageContent.id = 'messageContent' + receivedMessage.messageId;
                            messageContent.draggable = false;
                        } else {
                            messageContent = document.createElement('p');
                            messageContent.classList.add('message');
                            messageContent.style.marginLeft = '5px';
                            messageContent.style.fontSize = '14px';
                            messageContent.innerText = receivedMessage.content;
                            messageContent.id = 'messageContent' + receivedMessage.messageId;
                        }
                        div6.appendChild(messageContent);
                        div5.appendChild(div6);
                        div4.appendChild(div5);
                        div3.appendChild(div4);
                        div7.classList.add('row');
                        div8.classList.add('col', 'd-xl-flex', 'justify-content-xl-start');
                        dateElement.classList.add('.date');
                        dateElement.style.fontSize = '11px';
                        dateElement.innerText = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + ' ' + (date.getHours() + ':' + date.getMinutes());
                        div8.appendChild(dateElement);
                        div7.appendChild(div8);
                        div3.appendChild(div7);
                        div1.appendChild(div2);
                        div1.appendChild(div3);
                        messageArea.appendChild(div1);
                        chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
                    } else {
                        let editAndDeleteButtonsDiv = document.createElement('div');
                        let anotherEditAndDeleteDiv = document.createElement('div');
                        let deleteButton = document.createElement('button');
                        let deleteIcon = document.createElement('i');
                        let editButton = document.createElement('button');
                        let editIcon = document.createElement('i');
                        div1.id = 'message' + receivedMessage.messageId;
                        div1.classList.add('my_message', 'text');
                        div2.classList.add('col-sm-7', 'col-xl-6', 'offset-sm-3', 'offset-md-3', 'offset-lg-3', 'offset-xl-5');
                        usernameDiv.style.paddingRight = '10px';
                        usernameDiv.classList.add('justify-content-end');
                        div2.appendChild(usernameDiv);
                        div3.classList.add('row');
                        div4.classList.add('col-sm-12', 'col-xl-11', 'offset-xl-1');
                        editAndDeleteButtonsDiv.classList.add('row', 'edit_message_main');
                        anotherEditAndDeleteDiv.classList.add('col', 'd-flex', 'justify-content-start', 'align-items-start', 'edit_message');
                        deleteButton.style.border = 'none';
                        deleteButton.style.background = 'none';
                        deleteButton.style.paddingRight = '10px';
                        deleteButton.classList.add('deleteButton');
                        deleteIcon.classList.add('fas', 'fa-times');
                        editButton.style.border = 'none';
                        editButton.style.background = 'none';
                        editButton.style.paddingRight = '10px';
                        editButton.classList.add('editButton');
                        deleteButton.id = 'deleteMessage' + receivedMessage.messageId;
                        editButton.id = 'editMessage' + receivedMessage.messageId;
                        editIcon.classList.add('fas', 'fa-pencil-alt');
                        div4.style.padding = '5px';
                        div5.classList.add('col', 'd-sm-flex', 'd-md-flex', 'd-lg-flex', 'd-xl-flex', 'justify-content-sm-end', 'justify-content-md-end', 'justify-content-lg-end', 'justify-content-xl-end', 'align-items-xl-center');
                        if (isUrl(receivedMessage.content)) {
                            messageContent = document.createElement('a');
                            messageContent.classList.add('message', 'user_message');
                            messageContent.style.marginLeft = '5px';
                            messageContent.style.fontSize = '14px';
                            messageContent.href = receivedMessage.content;
                            messageContent.innerText = receivedMessage.content;
                            messageContent.target = '_blank';
                            messageContent.rel = 'noopener noreferrer';
                            messageContent.id = 'messageContent' + receivedMessage.messageId;
                            messageContent.draggable = false;
                        } else {
                            messageContent = document.createElement('p');
                            messageContent.classList.add('message', 'user_message');
                            messageContent.style.marginRight = '0';
                            messageContent.style.marginLeft = '0';
                            messageContent.style.fontSize = '14px';
                            messageContent.innerText = receivedMessage.content;
                            messageContent.id = 'messageContent' + receivedMessage.messageId;
                        }
                        div5.appendChild(messageContent);
                        deleteButton.appendChild(deleteIcon);
                        editButton.appendChild(editIcon);
                        anotherEditAndDeleteDiv.appendChild(deleteButton);
                        anotherEditAndDeleteDiv.appendChild(editButton);
                        editAndDeleteButtonsDiv.appendChild(anotherEditAndDeleteDiv);
                        editAndDeleteButtonsDiv.appendChild(div5);
                        div4.appendChild(editAndDeleteButtonsDiv);
                        div3.appendChild(div4);
                        div6.classList.add('row');
                        div7.classList.add('col', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-end', 'justify-content-xl-end');
                        dateElement.classList.add('.date');
                        dateElement.style.fontSize = '11px';
                        dateElement.innerText = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + ' ' + ('0' + date.getHours()).slice(-2) + ':' + ('0' + date.getMinutes()).slice(-2);
                        div7.appendChild(dateElement);
                        div6.appendChild(div7);
                        div8.classList.add('col-4', 'col-sm-2', 'col-lg-2', 'col-xl-1', 'offset-xl-0', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-center', 'align-items-sm-start');
                        messageLink.setAttribute('href', "/user/" + receivedMessage.id);
                        authorImg.classList.add('rounded-circle', 'image--cover');
                        authorImg.src = '/media/avatars/avatar' + receivedMessage.id + '.png';
                        authorImg.style.width = '50px';
                        authorImg.style.height = '50px';
                        messageLink.appendChild(authorImg);
                        div8.appendChild(messageLink);
                        div2.appendChild(div3);
                        div2.appendChild(div6);
                        div1.appendChild(div2);
                        div1.appendChild(div8);
                        messageArea.appendChild(div1);
                        chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
                    }
                } else if (receivedMessage.type === 'IMAGE') {
                    messagePreviewSpan.innerText = receivedMessage.sender + ': Фотография';
                    while (messagePreview.firstChild)
                        messagePreview.firstChild.remove();
                    messagePreview.appendChild(messagePreviewSpan);
                    let editAndDeleteButtonsDiv = document.createElement('div');
                    let anotherEditAndDeleteDiv = document.createElement('div');
                    let deleteButton = document.createElement('button');
                    let deleteIcon = document.createElement('i');
                    let date = new Date(receivedMessage.timestamp);
                    let div1 = document.createElement('div');
                    let div2 = document.createElement('div');
                    let div3 = document.createElement('div');
                    let div4 = document.createElement('div');
                    let div5 = document.createElement('div');
                    let div6 = document.createElement('div');
                    let div7 = document.createElement('div');
                    let div8 = document.createElement('div');
                    let fancyBox = document.createElement('a');
                    let dateElement = document.createElement('span');
                    let messageLink = document.createElement('a');
                    let authorImg = document.createElement('img');
                    let messageContent = document.createElement('img');
                    div1.classList.add('row');
                    div1.style.padding = '10px';
                    if (id !== receivedMessage.id) {
                        div1.classList.add('row', 'opponents_message');
                        div1.id = 'message' + receivedMessage.messageId;
                        div2.classList.add('col-4', 'col-sm-2', 'col-lg-2', 'col-xl-1', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-center', 'align-items-sm-start');
                        messageLink.setAttribute('href', "/user/" + receivedMessage.id);
                        authorImg.classList.add('rounded-circle', 'image--cover');
                        authorImg.src = '/media/avatars/avatar' + receivedMessage.id + '.png';
                        authorImg.style.width = '50px';
                        authorImg.style.height = '50px';
                        messageLink.appendChild(authorImg);
                        div2.appendChild(messageLink);
                        div3.classList.add('col-sm-7', 'col-xl-4');
                        usernameDiv.style.paddingLeft = '10px';
                        usernameDiv.classList.add('justify-content-start');
                        div3.appendChild(usernameDiv);
                        div4.classList.add('row');
                        div5.classList.add('col-sm-12', 'col-xl-11', 'offset-xl-0');
                        div5.style.padding = '5px';
                        div6.classList.add('d-sm-flex', 'd-md-flex', 'd-lg-flex', 'd-xl-flex', 'justify-content-sm-start', 'justify-content-md-start', 'justify-content-lg-start', 'justify-content-xl-start', 'align-items-xl-center');
                        messageContent.classList.add('message');
                        messageContent.style.marginLeft = '5px';
                        messageContent.style.width = '100%';
                        messageContent.style.fontSize = '14px';
                        messageContent.src = receivedMessage.content.substr(1);
                        messageContent.style.maxWidth = '300px';
                        messageContent.style.maxHeight = '300px';
                        messageContent.id = 'messageContent' + receivedMessage.messageId;
                        fancyBox.appendChild(messageContent);
                        fancyBox.classList.add('image_message');
                        fancyBox.href = receivedMessage.content.substr(1);
                        div6.appendChild(fancyBox);
                        div5.appendChild(div6);
                        div4.appendChild(div5);
                        div3.appendChild(div4);
                        div7.classList.add('row');
                        div8.classList.add('col', 'd-xl-flex', 'justify-content-xl-start');
                        dateElement.classList.add('.date');
                        dateElement.style.fontSize = '11px';
                        dateElement.innerText = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + ' ' + (date.getHours() - 2 + ':' + date.getMinutes());
                        div8.appendChild(dateElement);
                        div7.appendChild(div8);
                        div3.appendChild(div7);
                        div1.appendChild(div2);
                        div1.appendChild(div3);
                        messageArea.appendChild(div1);
                        chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
                    } else {
                        div1.id = 'message' + receivedMessage.messageId;
                        div2.classList.add('col-sm-7', 'col-xl-6', 'offset-sm-3', 'offset-md-3', 'offset-lg-3', 'offset-xl-5');
                        usernameDiv.style.paddingRight = '10px';
                        usernameDiv.classList.add('justify-content-end');
                        div2.appendChild(usernameDiv);
                        div3.classList.add('row');
                        div4.classList.add('col-sm-12', 'col-xl-11', 'offset-xl-1');
                        div4.style.padding = '5px';
                        div5.classList.add('col', 'd-sm-flex', 'd-md-flex', 'd-lg-flex', 'd-xl-flex', 'justify-content-sm-end', 'justify-content-md-end', 'justify-content-lg-end', 'justify-content-xl-end', 'align-items-xl-center');
                        messageContent.classList.add('message', 'user_message');
                        messageContent.style.marginRight = '0';
                        messageContent.style.marginLeft = '0';
                        messageContent.style.fontSize = '14px';
                        messageContent.src = receivedMessage.content.substr(1);
                        messageContent.style.maxWidth = '300px';
                        messageContent.style.maxHeight = '300px';
                        editAndDeleteButtonsDiv.classList.add('row', 'edit_message_main');
                        anotherEditAndDeleteDiv.classList.add('col', 'd-flex', 'justify-content-start', 'align-items-start', 'edit_message');
                        deleteButton.style.border = 'none';
                        deleteButton.style.background = 'none';
                        deleteButton.style.paddingRight = '10px';
                        deleteButton.id = 'deleteMessage' + receivedMessage.messageId;
                        deleteButton.classList.add('deleteButton');
                        deleteIcon.classList.add('fas', 'fa-times');
                        deleteButton.appendChild(deleteIcon);
                        anotherEditAndDeleteDiv.appendChild(deleteButton);
                        editAndDeleteButtonsDiv.appendChild(anotherEditAndDeleteDiv);
                        fancyBox.appendChild(messageContent);
                        fancyBox.classList.add('image_message');
                        fancyBox.href = receivedMessage.content.substr(1);
                        div5.appendChild(fancyBox);
                        editAndDeleteButtonsDiv.appendChild(div5);
                        div4.appendChild(editAndDeleteButtonsDiv);
                        div3.appendChild(div4);
                        div6.classList.add('row');
                        div7.classList.add('col', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-end', 'justify-content-xl-end');
                        dateElement.classList.add('.date');
                        dateElement.style.fontSize = '11px';
                        dateElement.innerText = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + ' ' + (date.getHours() - 2 + ':' + date.getMinutes());
                        div7.appendChild(dateElement);
                        div6.appendChild(div7);
                        div8.classList.add('col-4', 'col-sm-2', 'col-lg-2', 'col-xl-1', 'offset-xl-0', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-center', 'align-items-sm-start');
                        messageLink.setAttribute('href', "/user/" + receivedMessage.id);
                        messageLink.id = 'messageContent' + receivedMessage.messageId;
                        authorImg.classList.add('rounded-circle', 'image--cover');
                        authorImg.src = '/media/avatars/avatar' + receivedMessage.id + '.png';
                        authorImg.style.width = '50px';
                        authorImg.style.height = '50px';
                        messageLink.appendChild(authorImg);
                        div8.appendChild(messageLink);
                        div2.appendChild(div3);
                        div2.appendChild(div6);
                        div1.appendChild(div2);
                        div1.appendChild(div8);
                        messageArea.appendChild(div1);
                        chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
                    }
                } else if (receivedMessage.type === 'AUDIO') {
                    messagePreviewSpan.innerText = receivedMessage.sender + ': Аудиозапись';
                    while (messagePreview.firstChild)
                        messagePreview.firstChild.remove();
                    messagePreview.appendChild(messagePreviewSpan);
                    let editAndDeleteButtonsDiv = document.createElement('div');
                    let anotherEditAndDeleteDiv = document.createElement('div');
                    let deleteButton = document.createElement('button');
                    let deleteIcon = document.createElement('i');
                    let date = new Date(receivedMessage.timestamp);
                    let parentDiv = document.createElement('div');
                    let fileName = document.createElement('span');
                    let div1 = document.createElement('div');
                    let div2 = document.createElement('div');
                    let div3 = document.createElement('div');
                    let div4 = document.createElement('div');
                    let div5 = document.createElement('div');
                    let div6 = document.createElement('div');
                    let div7 = document.createElement('div');
                    let div8 = document.createElement('div');
                    let dateElement = document.createElement('span');
                    let messageLink = document.createElement('a');
                    let authorImg = document.createElement('img');
                    let messageContent = document.createElement('audio');
                    let source = document.createElement('source');
                    parentDiv.style.textAlign = 'center';
                    parentDiv.style.background = 'none';
                    parentDiv.classList.add('message');
                    div1.classList.add('row');
                    div1.style.padding = '10px';
                    if (id !== receivedMessage.id) {
                        div1.classList.add('opponents_message');
                        div1.id = 'message' + receivedMessage.messageId;
                        div2.classList.add('col-4', 'col-sm-2', 'col-lg-2', 'col-xl-1', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-center', 'align-items-sm-start');
                        messageLink.setAttribute('href', "/user/" + receivedMessage.id);
                        authorImg.classList.add('rounded-circle', 'image--cover');
                        authorImg.src = '/media/avatars/avatar' + receivedMessage.id + '.png';
                        authorImg.style.width = '50px';
                        authorImg.style.height = '50px';
                        messageLink.appendChild(authorImg);
                        div2.appendChild(messageLink);
                        div3.classList.add('col-sm-7', 'col-xl-4');
                        usernameDiv.style.paddingLeft = '10px';
                        usernameDiv.classList.add('justify-content-start');
                        div3.appendChild(usernameDiv);
                        div4.classList.add('row');
                        div5.classList.add('col-sm-12', 'col-xl-11', 'offset-xl-0');
                        div5.style.padding = '5px';
                        div6.classList.add('d-sm-flex', 'd-md-flex', 'd-lg-flex', 'd-xl-flex', 'justify-content-sm-start', 'justify-content-md-start', 'justify-content-lg-start', 'justify-content-xl-start', 'align-items-xl-center');
                        messageContent.classList.add('message');
                        messageContent.setAttribute('controls', 'controls');
                        messageContent.setAttribute('preload', 'metadata');
                        source.src = receivedMessage.content.substr(1);
                        messageContent.id = 'messageContent' + receivedMessage.messageId;
                        messageContent.appendChild(source);
                        messageContent.addEventListener("play", stopAll, null);
                        fileName.innerText = receivedMessage.content.substring(receivedMessage.content.lastIndexOf("/") + 1, receivedMessage.content.length);
                        parentDiv.appendChild(fileName);
                        parentDiv.appendChild(messageContent);
                        div6.appendChild(parentDiv);
                        div5.appendChild(div6);
                        div4.appendChild(div5);
                        div3.appendChild(div4);
                        div7.classList.add('row');
                        div8.classList.add('col', 'd-xl-flex', 'justify-content-xl-start');
                        dateElement.classList.add('.date');
                        dateElement.style.fontSize = '11px';
                        dateElement.innerText = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + ' ' + (date.getHours() + ':' + date.getMinutes());
                        div8.appendChild(dateElement);
                        div7.appendChild(div8);
                        div3.appendChild(div7);
                        div1.appendChild(div2);
                        div1.appendChild(div3);
                        messageArea.appendChild(div1);
                        chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
                    } else {
                        div1.id = 'message' + receivedMessage.messageId;
                        div2.classList.add('col-sm-7', 'col-xl-6', 'offset-sm-3', 'offset-md-3', 'offset-lg-3', 'offset-xl-5');
                        usernameDiv.style.paddingRight = '10px';
                        usernameDiv.classList.add('justify-content-end');
                        div2.appendChild(usernameDiv);
                        div3.classList.add('row');
                        div4.classList.add('col-sm-12', 'col-xl-11', 'offset-xl-1');
                        div4.style.padding = '5px';
                        div5.classList.add('col', 'd-sm-flex', 'd-md-flex', 'd-lg-flex', 'd-xl-flex', 'justify-content-sm-end', 'justify-content-md-end', 'justify-content-lg-end', 'justify-content-xl-end', 'align-items-xl-center');
                        messageContent.setAttribute('controls', 'controls');
                        messageContent.setAttribute('preload', 'metadata');
                        messageContent.id = 'messageContent' + receivedMessage.messageId;
                        source.src = receivedMessage.content.substr(1);
                        messageContent.appendChild(source);
                        messageContent.addEventListener("play", stopAll, null);
                        editAndDeleteButtonsDiv.classList.add('row', 'edit_message_main');
                        anotherEditAndDeleteDiv.classList.add('col', 'd-flex', 'justify-content-start', 'align-items-start', 'edit_message');
                        deleteButton.style.border = 'none';
                        deleteButton.style.background = 'none';
                        deleteButton.style.paddingRight = '10px';
                        deleteButton.id = 'deleteMessage' + receivedMessage.messageId;
                        deleteButton.classList.add('deleteButton');
                        deleteIcon.classList.add('fas', 'fa-times');
                        deleteButton.appendChild(deleteIcon);
                        anotherEditAndDeleteDiv.appendChild(deleteButton);
                        editAndDeleteButtonsDiv.appendChild(anotherEditAndDeleteDiv);
                        fileName.innerText = receivedMessage.content.substring(receivedMessage.content.lastIndexOf("/") + 1, receivedMessage.content.length);
                        parentDiv.appendChild(fileName);
                        parentDiv.appendChild(messageContent);
                        div5.appendChild(parentDiv);
                        editAndDeleteButtonsDiv.appendChild(div5);
                        div4.appendChild(editAndDeleteButtonsDiv);
                        div3.appendChild(div4);
                        div6.classList.add('row');
                        div7.classList.add('col', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-end', 'justify-content-xl-end');
                        dateElement.classList.add('.date');
                        dateElement.style.fontSize = '11px';
                        dateElement.innerText = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + ' ' + (date.getHours() + ':' + date.getMinutes());
                        div7.appendChild(dateElement);
                        div6.appendChild(div7);
                        div8.classList.add('col-4', 'col-sm-2', 'col-lg-2', 'col-xl-1', 'offset-xl-0', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-center', 'align-items-sm-start');
                        messageLink.setAttribute('href', "/user/" + receivedMessage.id);
                        messageLink.id = 'messageContent' + receivedMessage.messageId;
                        authorImg.classList.add('rounded-circle', 'image--cover');
                        authorImg.src = '/media/avatars/avatar' + receivedMessage.id + '.png';
                        authorImg.style.width = '50px';
                        authorImg.style.height = '50px';
                        messageLink.appendChild(authorImg);
                        div8.appendChild(messageLink);
                        div2.appendChild(div3);
                        div2.appendChild(div6);
                        div1.appendChild(div2);
                        div1.appendChild(div8);
                        messageArea.appendChild(div1);
                        chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
                    }
                } else if (receivedMessage.type === 'VIDEO') {
                    messagePreviewSpan.innerText = receivedMessage.sender + ': Видеозапись';
                    while (messagePreview.firstChild)
                        messagePreview.firstChild.remove();
                    messagePreview.appendChild(messagePreviewSpan);
                    let editAndDeleteButtonsDiv = document.createElement('div');
                    let anotherEditAndDeleteDiv = document.createElement('div');
                    let deleteButton = document.createElement('button');
                    let deleteIcon = document.createElement('i');
                    let date = new Date(receivedMessage.timestamp);
                    let parentDiv = document.createElement('div');
                    let fileName = document.createElement('span');
                    let div1 = document.createElement('div');
                    let div2 = document.createElement('div');
                    let div3 = document.createElement('div');
                    let div4 = document.createElement('div');
                    let div5 = document.createElement('div');
                    let div6 = document.createElement('div');
                    let div7 = document.createElement('div');
                    let div8 = document.createElement('div');
                    let dateElement = document.createElement('span');
                    let messageLink = document.createElement('a');
                    let authorImg = document.createElement('img');
                    let messageContent = document.createElement('video');
                    let source = document.createElement('source');
                    parentDiv.style.textAlign = 'center';
                    parentDiv.style.background = 'none';
                    parentDiv.classList.add('message');
                    div1.classList.add('row');
                    div1.style.padding = '10px';
                    if (id !== receivedMessage.id) {
                        div1.classList.add('opponents_message');
                        div1.id = 'message' + receivedMessage.messageId;
                        div2.classList.add('col-4', 'col-sm-2', 'col-lg-2', 'col-xl-1', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-center', 'align-items-sm-start');
                        messageLink.setAttribute('href', "/user/" + receivedMessage.id);
                        authorImg.classList.add('rounded-circle', 'image--cover');
                        authorImg.src = '/media/avatars/avatar' + receivedMessage.id + '.png';
                        authorImg.style.width = '50px';
                        authorImg.style.height = '50px';
                        messageLink.appendChild(authorImg);
                        div2.appendChild(messageLink);
                        div3.classList.add('col-sm-7', 'col-xl-4');
                        usernameDiv.style.paddingLeft = '10px';
                        usernameDiv.classList.add('justify-content-start');
                        div3.appendChild(usernameDiv);
                        div4.classList.add('row');
                        div5.classList.add('col-sm-12', 'col-xl-11', 'offset-xl-0');
                        div5.style.padding = '5px';
                        div6.classList.add('d-sm-flex', 'd-md-flex', 'd-lg-flex', 'd-xl-flex', 'justify-content-sm-start', 'justify-content-md-start', 'justify-content-lg-start', 'justify-content-xl-start', 'align-items-xl-center');
                        messageContent.classList.add('message');
                        messageContent.setAttribute('controls', 'controls');
                        messageContent.setAttribute('preload', 'metadata');
                        source.src = receivedMessage.content.substr(1);
                        messageContent.id = 'messageContent' + receivedMessage.messageId;
                        messageContent.appendChild(source);
                        messageContent.style.maxHeight = '500px';
                        messageContent.style.maxWidth = '500px';
                        messageContent.addEventListener("play", stopAll, null);
                        fileName.innerText = receivedMessage.content.substring(receivedMessage.content.lastIndexOf("/") + 1, receivedMessage.content.length);
                        parentDiv.appendChild(fileName);
                        parentDiv.appendChild(messageContent);
                        div6.appendChild(parentDiv);
                        div5.appendChild(div6);
                        div4.appendChild(div5);
                        div3.appendChild(div4);
                        div7.classList.add('row');
                        div8.classList.add('col', 'd-xl-flex', 'justify-content-xl-start');
                        dateElement.classList.add('.date');
                        dateElement.style.fontSize = '11px';
                        dateElement.innerText = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + ' ' + (date.getHours() + ':' + date.getMinutes());
                        div8.appendChild(dateElement);
                        div7.appendChild(div8);
                        div3.appendChild(div7);
                        div1.appendChild(div2);
                        div1.appendChild(div3);
                        messageArea.appendChild(div1);
                        chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
                    } else {
                        div1.id = 'message' + receivedMessage.messageId;
                        div2.classList.add('col-sm-7', 'col-xl-6', 'offset-sm-3', 'offset-md-3', 'offset-lg-3', 'offset-xl-5');
                        usernameDiv.style.paddingRight = '10px';
                        usernameDiv.classList.add('justify-content-end');
                        div2.appendChild(usernameDiv);
                        div3.classList.add('row');
                        div4.classList.add('col-sm-12', 'col-xl-11', 'offset-xl-1');
                        div4.style.padding = '5px';
                        div5.classList.add('col', 'd-sm-flex', 'd-md-flex', 'd-lg-flex', 'd-xl-flex', 'justify-content-sm-end', 'justify-content-md-end', 'justify-content-lg-end', 'justify-content-xl-end', 'align-items-xl-center');
                        messageContent.setAttribute('controls', 'controls');
                        messageContent.setAttribute('preload', 'metadata');
                        messageContent.style.maxHeight = '500px';
                        messageContent.style.maxWidth = '500px';
                        messageContent.id = 'messageContent' + receivedMessage.messageId;
                        source.src = receivedMessage.content.substr(1);
                        messageContent.appendChild(source);
                        messageContent.addEventListener("play", stopAll, null);
                        editAndDeleteButtonsDiv.classList.add('row', 'edit_message_main');
                        anotherEditAndDeleteDiv.classList.add('col', 'd-flex', 'justify-content-start', 'align-items-start', 'edit_message');
                        deleteButton.style.border = 'none';
                        deleteButton.style.background = 'none';
                        deleteButton.style.paddingRight = '10px';
                        deleteButton.id = 'deleteMessage' + receivedMessage.messageId;
                        deleteButton.classList.add('deleteButton');
                        deleteIcon.classList.add('fas', 'fa-times');
                        deleteButton.appendChild(deleteIcon);
                        anotherEditAndDeleteDiv.appendChild(deleteButton);
                        editAndDeleteButtonsDiv.appendChild(anotherEditAndDeleteDiv);
                        fileName.innerText = receivedMessage.content.substring(receivedMessage.content.lastIndexOf("/") + 1, receivedMessage.content.length);
                        parentDiv.appendChild(fileName);
                        parentDiv.appendChild(messageContent);
                        div5.appendChild(parentDiv);
                        editAndDeleteButtonsDiv.appendChild(div5);
                        div4.appendChild(editAndDeleteButtonsDiv);
                        div3.appendChild(div4);
                        div6.classList.add('row');
                        div7.classList.add('col', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-end', 'justify-content-xl-end');
                        dateElement.classList.add('.date');
                        dateElement.style.fontSize = '11px';
                        dateElement.innerText = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + ' ' + (date.getHours() + ':' + date.getMinutes());
                        div7.appendChild(dateElement);
                        div6.appendChild(div7);
                        div8.classList.add('col-4', 'col-sm-2', 'col-lg-2', 'col-xl-1', 'offset-xl-0', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-center', 'align-items-sm-start');
                        messageLink.setAttribute('href', "/user/" + receivedMessage.id);
                        messageLink.id = 'messageContent' + receivedMessage.messageId;
                        authorImg.classList.add('rounded-circle', 'image--cover');
                        authorImg.src = '/media/avatars/avatar' + receivedMessage.id + '.png';
                        authorImg.style.width = '50px';
                        authorImg.style.height = '50px';
                        messageLink.appendChild(authorImg);
                        div8.appendChild(messageLink);
                        div2.appendChild(div3);
                        div2.appendChild(div6);
                        div1.appendChild(div2);
                        div1.appendChild(div8);
                        messageArea.appendChild(div1);
                        chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
                    }
                } else if (receivedMessage.type === 'UPDATE') {
                    let message = document.getElementById('messageContent' + receivedMessage.messageId);
                    message.textContent = receivedMessage.content;
                } else if (receivedMessage.type === 'DELETE') {
                    let message = document.getElementById('message' + receivedMessage.messageId);
                    messageArea.removeChild(message);
                }
            } else {
                let messagePreview = document.getElementById('messagePreview' + receivedMessage.roomId);
                if (typeof messagePreview !== "undefined") {
                    let messagePreviewSpan = document.createElement('span');
                    let messagePreviewImg = document.getElementById('imgPreview' + receivedMessage.roomId);
                    if (messagePreviewImg !== null) {
                        messagePreviewImg.src = '/media/avatars/avatar' + receivedMessage.id + '.png';
                        if (receivedMessage.type === 'CHAT') {
                            messagePreviewSpan.innerText = receivedMessage.sender + ': ' + receivedMessage.content;
                            while (messagePreview.firstChild)
                                messagePreview.firstChild.remove();
                            messagePreview.appendChild(messagePreviewSpan);
                        } else if (receivedMessage.type === 'IMAGE') {
                            messagePreviewSpan.innerText = receivedMessage.sender + ': Фотография';
                            while (messagePreview.firstChild)
                                messagePreview.firstChild.remove();
                            messagePreview.appendChild(messagePreviewSpan);
                        } else if (receivedMessage.type === 'AUDIO') {
                            messagePreviewSpan.innerText = receivedMessage.sender + ': Аудиозапись';
                            while (messagePreview.firstChild)
                                messagePreview.firstChild.remove();
                            messagePreview.appendChild(messagePreviewSpan);
                        } else if (receivedMessage.type === 'VIDEO') {
                            messagePreviewSpan.innerText = receivedMessage.sender + ': Видеозапись';
                            while (messagePreview.firstChild)
                                messagePreview.firstChild.remove();
                            messagePreview.appendChild(messagePreviewSpan);
                        } else if (receivedMessage.type === 'UPDATE') {
                            let previewMessageExistingSpan = document.getElementById('messagePreviewSpan' + receivedMessage.id);
                            if (typeof previewMessageExistingSpan !== "undefined") {
                                messagePreviewSpan.innerText = receivedMessage.sender + ': ' + receivedMessage.content;
                                while (messagePreview.firstChild)
                                    messagePreview.firstChild.remove();
                                messagePreview.appendChild(messagePreviewSpan);
                            }
                        } else if (receivedMessage.type === 'DELETE') {
                            let previewMessageExistingSpan = document.getElementById('messagePreviewSpan' + receivedMessage.id);
                            if (typeof previewMessageExistingSpan !== "undefined") {
                                messagePreviewSpan.innerText = ""; //todo:AJAX CALL
                                while (messagePreview.firstChild)
                                    messagePreview.firstChild.remove();
                                messagePreview.appendChild(messagePreviewSpan);
                            }
                        }
                    }
                }
            }
        }
    }


    window.addEventListener('paste', e => {
        document.getElementById('file').files = e.clipboardData.files;
    });


    function sendEditedMessage(event) {
        event.preventDefault();
        let fileButton = document.getElementById('fileLabel');
        let sendButton = document.getElementById('send');
        let editButton = document.getElementById('edit');
        let cancelEditButton = document.getElementById('cancelEdit');
        let messageContent = messageInput.value.trim();
        if (messageContent && stompClient) {
            let chatMessage = {
                roomId: roomId.textContent,
                messageId: currentEditMessageId,
                id: id,
                sender: username,
                login: login,
                content: messageInput.value,
                type: 'UPDATE',
                timestamp: date
            };
            stompClient.send("/app/chat.saveAndSendMessage." + roomId.textContent, {}, JSON.stringify(chatMessage));
            messageInput.value = '';
            fileButton.style.display = 'inline-block';
            sendButton.style.display = 'inline-block';
            editButton.style.display = 'none';
            cancelEditButton.style.display = 'none';
            currentEditMessageId = null;
        }
    }


    $(document).on("click", '.editButton', function (event) {
        event.preventDefault();
        let messageId = event.currentTarget.id.match(/\d+/g);
        messageInput.value = document.getElementById('messageContent' + messageId).textContent;
        let fileButton = document.getElementById('fileLabel');
        let sendButton = document.getElementById('send');
        let editButton = document.getElementById('edit');
        let cancelEditButton = document.getElementById('cancelEdit');
        fileButton.style.display = 'none';
        sendButton.style.display = 'none';
        editButton.style.display = 'inline-block';
        cancelEditButton.style.display = 'inline-block';
        currentEditMessageId = messageId.toString();
    });


    $(document).on('click', '.deleteButton', function (event) {
        event.preventDefault();
        let messageId = event.currentTarget.id.match(/\d+/g);
        let chatMessage = {
            roomId: roomId.textContent,
            messageId: messageId.toString(),
            id: id,
            sender: username,
            login: login,
            content: messageInput.value,
            type: 'DELETE',
            timestamp: date
        };
        stompClient.send("/app/chat.saveAndSendMessage." + roomId.textContent, {}, JSON.stringify(chatMessage));
    });


    if (cancelEdit != null)
        cancelEdit.addEventListener('click', function (event) {
            event.preventDefault();
            let fileButton = document.getElementById('fileLabel');
            let o = document.getElementById("progress");
            let file = document.getElementById('file');
            let sendButton = document.getElementById('send');
            let editButton = document.getElementById('edit');
            let cancelEditButton = document.getElementById('cancelEdit');
            let fileDrag = document.getElementById('filedragDIV');
            fileDrag.style.display = 'none';
            file.value = '';
            o.innerHTML = '';
            fileButton.style.display = 'inline-block';
            sendButton.style.display = 'inline-block';
            editButton.style.display = 'none';
            cancelEditButton.style.display = 'none';
            messageInput.value = '';
            currentEditMessageId = null;
        });
    if (sendButton != null)
        sendButton.addEventListener('click', function (event) {
            if (disabled === false) {
                sendMessage(event);
                disableSendButton();
                setTimeout(enableSendButton, 3000);
            }
        });

    if (editButton != null)
        editButton.addEventListener('click', function (event) {
            sendEditedMessage(event);
        });


    if (typeof editChatTitleButton !== "undefined" && editChatTitleButton !== null)
        editChatTitleButton.addEventListener('click', function (event) {
            event.preventDefault();
            chatTitle.style.display = 'none';
            editChatTitleButton.style.display = 'none';
            editChatTitleInput.style.display = 'inline-block';
            editChatTitleInput.value = chatTitle.innerText;
            editChatTitleInput.focus();
            confirmEditChatTitle.style.display = 'inline-block';
        });


    confirmEditChatTitle.addEventListener('click', function (event) {
        event.preventDefault();
        let inputValue = editChatTitleInput.value;
        if (inputValue !== null && inputValue.trim() !== "") {
            let editChatTitleMessage = {
                roomId: roomId.textContent,
                title: editChatTitleInput.value
            };
            stompClient.send("/app/chat.editChatTitle." + roomId.textContent, {}, JSON.stringify(editChatTitleMessage));
        }
        chatTitle.style.display = 'inline-block';
        editChatTitleButton.style.display = 'inline-block';
        editChatTitleInput.style.display = 'none';
        confirmEditChatTitle.style.display = 'none';
    });


    function onTitleChange(payload) {
        let editChatTitleMessage = JSON.parse(payload.body);
        if (roomId.textContent === editChatTitleMessage.roomId) {
            chatTitle.textContent = editChatTitleMessage.title;
            let chatListElem = document.getElementById('chatTitle' + roomId.textContent);
            chatListElem.textContent = editChatTitleMessage.title;
        }
    }


    function onRemoveRoomMember(payload) {
        let removeRoomMemberMessage = JSON.parse(payload.body);
        if (roomId.textContent === removeRoomMemberMessage.roomId) {
            if (removeRoomMemberMessage.memberId === id)
                window.location.replace('/chats');
            else {
                document.getElementById('membersList').removeChild(document.getElementById('removeMember' + removeRoomMemberMessage.memberId));
                let onlineElem = document.getElementById('online' + removeRoomMemberMessage.memberId);
                let offlineElem = document.getElementById('offline' + removeRoomMemberMessage.memberId);
                if (onlineElem !== null)
                    document.getElementById('onlineUsers').removeChild(onlineElem);
                if (offlineElem !== null)
                    document.getElementById('offlineUsers').removeChild(offlineElem);
                let addMemberForm = document.getElementById('addMemberForm');
                addMemberForm.removeChild(document.getElementById('addMember' + removeRoomMemberMessage.memberId));
            }
        }
    }


    function textarea_resize(event, line_height, min_line_count) {
        let min_line_height = min_line_count * line_height;
        let obj = event.target;
        let div = document.getElementById(obj.id + '_div');
        div.innerHTML = obj.value;
        let obj_height = div.offsetHeight;
        if (event.keyCode === 13)
            obj_height += line_height;
        else if (obj_height < min_line_height)
            obj_height = min_line_height;
        obj.style.height = obj_height + 'px';
    }


    $('#message').bind("keypress", function (e) {
        textarea_resize(e, 15, 2);
    });


    let searchMessageSubmit = document.getElementById('searchMessageSubmit');
    let searchMessageInput = document.getElementById('searchMessageInput');


    if (searchMessageSubmit != null)
        searchMessageSubmit.addEventListener('click', function (event) {
            if (searchMessageInput.value.trim() === "") {
                event.preventDefault();
                if (searchMessageInput.style.width === '0px') {
                    searchMessageInput.style.width = '10vw';
                    searchMessageInput.style.opacity = '1';
                } else {
                    searchMessageInput.style.width = '0';
                    searchMessageInput.style.opacity = '0';
                }
            }
        });


    if (searchMessageInput != null)
        searchMessageInput.value = "";


    $('#viewAttachments').on('click', function () {
        let textMessages = document.getElementsByClassName('text');
        Array.from(textMessages).forEach(element => {
            document.getElementById('message' + element.id.match(/\d+/g)).style.display = 'none';
        });
        document.getElementById("viewAttachmentsCancel").style.display = 'inline';
        disableMessaging = true;
    });


    $('#viewAttachmentsCancel').on('click', function () {
        if (isSearching === 'true')
            window.history.back();
        else
            window.location.reload(true);
    });


    if (isSearching === 'true')
        document.getElementById('viewAttachmentsCancel').style.display = 'inline';


    $("#messageArea").on("focusin", function () {
        $("a.image_message").fancybox({
            opacity: true,
            overlayShow: false,
            transitionIn: 'elastic',
            transitionOut: 'fade',
            aspectRatio: true
        });
    });
});