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

    connectingError.classList.add('hidden', 'text-danger', 'text-center');
    connectingError.id = 'connectionError';
    if (!stompClient)
        connect();
    chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
    messageInput.focus();


    function connect() {
        id = document.querySelector('#id').textContent.trim();
        username = document.querySelector('#username').innerText.trim();
        login = document.querySelector('#login').textContent.trim();
        if (username) {
            let socket = new SockJS('https://' + window.location.host + '/ws');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, onConnected, onError);
            stompClient.debug = true;
        }
    }

    $('#messageSendForm').bind("keypress", function (e) {
        if (e.keyCode === 13 && currentEditMessageId === null)
            sendMessage(e);
        else if (e.keyCode === 13 && currentEditMessageId !== null)
            sendEditedMessage(e);
    });


    $('#send').click(sendMessage(event));


    function onConnected() {
        stompClient.subscribe('/topic/chat/' + roomId.textContent, onMessageReceived);
        stompClient.subscribe('/topic/onlineTracker', onStatusChange);
    }


    function onError() {
        connectingError.style.visibility = "visible";
        connectingError.textContent = 'Невозможно подключится к серверу, попробуйте позже';
        messageArea.appendChild(connectingError);
    }


    function onStatusChange(payload) {
        let statusChangeMessage = JSON.parse(payload.body);
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
            let checkOnline = document.getElementById('online' + statusChangeMessage.id);
            if (!checkOnline) {
                a.setAttribute('href', "/user/" + statusChangeMessage.id);
                div1.classList.add('row', 'userStatus');
                div1.style.padding = '10px';
                div1.id = 'online' + statusChangeMessage.id;
                div2.style.padding = '0px';
                div2.classList.add('col-4', 'col-sm-7', 'col-md-5', 'col-lg-3', 'col-xl-3', 'offset-xl-0', 'd-sm-flex', 'd-md-flex', 'd-lg-flex', 'justify-content-sm-center', 'align-items-sm-center', 'justify-content-md-center', 'align-items-md-center', 'justify-content-lg-center', 'align-items-lg-center');
                img.classList.add('rounded-circle', 'd-lg-flex', 'justify-content-lg-center', 'align-items-lg-center');
                img.src = '/media/avatars/avatar' + statusChangeMessage.id + '.png';
                img.style.width = '35px';
                img.style.height = '35px';
                div2.appendChild(img);
                div1.appendChild(div2);
                div3.classList.add('col-8', 'col-sm-12', 'col-md-7', 'col-xl-7', 'offset-xl-0', 'd-xl-flex', 'justify-content-xl-start', 'align-items-xl-center');
                div3.style.padding = '0px';
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
            let checkOffline = document.getElementById('offline' + statusChangeMessage.id);
            if (!checkOffline) {
                a.setAttribute('href', "/user/" + statusChangeMessage.id);
                div1.classList.add('row', 'userStatus');
                div1.style.padding = '10px';
                div1.id = 'offline' + statusChangeMessage.id;
                div2.style.padding = '0px';
                div2.classList.add('col-4', 'col-sm-7', 'col-md-5', 'col-lg-3', 'col-xl-3', 'offset-xl-0', 'd-sm-flex', 'd-md-flex', 'd-lg-flex', 'justify-content-sm-center', 'align-items-sm-center', 'justify-content-md-center', 'align-items-md-center', 'justify-content-lg-center', 'align-items-lg-center');
                img.classList.add('rounded-circle', 'd-lg-flex', 'justify-content-lg-center', 'align-items-lg-center');
                img.src = '/media/avatars/avatar' + statusChangeMessage.id + '.png';
                img.style.width = '35px';
                img.style.height = '35px';
                div2.appendChild(img);
                div1.appendChild(div2);
                div3.classList.add('col-8', 'col-sm-12', 'col-md-7', 'col-xl-7', 'offset-xl-0', 'd-xl-flex', 'justify-content-xl-start', 'align-items-xl-center');
                div3.style.padding = '0px';
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
        let file = $('#file');
        if (file.val() && stompClient) {
            let form_data = new FormData();
            let file_data = file.prop('files');
            for (let i = 0; i < file_data.length; i++) {
                form_data.append('file' + i, file_data[i]);
            }
            form_data.append('id', $('#id').text());
            form_data.append('timestamp', new Date().getTime().toString());
            form_data.append('roomId', roomId.textContent);
            let token = $("meta[name='_csrf']").attr("content");
            $.ajax({
                url: "/chat/" + roomId.textContent,
                type: "POST",
                headers: {"X-CSRF-TOKEN": token},
                data: form_data,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
            });
            file.val(null);
        }
    }


    function onMessageReceived(payload) {
        let receivedMessage = JSON.parse(payload.body);
        if (receivedMessage.type === 'CHAT') {
            let date = new Date(receivedMessage.timestamp);
            let month = date.getMonth();
            let currentHours = date.getHours();
            let currentMinutes = date.getMinutes();
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
            let messageContent = document.createElement('p');
            month += 1;
            currentHours = ("0" + currentHours).slice(-2);
            currentMinutes = ("0" + currentMinutes).slice(-2);
            div1.classList.add('row');
            div1.style.padding = '10px';
            if (id !== receivedMessage.id) {
                div1.classList.add('opponents_message');
                div1.id = 'message' + receivedMessage.messageId;
                div2.classList.add('col-4', 'col-sm-2', 'col-lg-2', 'col-xl-1', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-center', 'align-items-sm-start');
                messageLink.setAttribute('href', "/user/" + receivedMessage.id);
                authorImg.classList.add('rounded-circle', 'd-xl-flex', 'justify-content-xl-center', 'align-items-xl-center');
                authorImg.src = '/media/avatars/avatar' + receivedMessage.id + '.png';
                authorImg.style.width = '50px';
                authorImg.style.height = '50px';
                messageLink.appendChild(authorImg);
                div2.appendChild(messageLink);
                div3.classList.add('col-sm-7', 'col-xl-6');
                div4.classList.add('row');
                div5.classList.add('col-sm-12', 'col-xl-11', 'offset-xl-0');
                div5.style.padding = '5px';
                div6.classList.add('d-sm-flex', 'd-md-flex', 'd-lg-flex', 'd-xl-flex', 'justify-content-sm-start', 'justify-content-md-start', 'justify-content-lg-start', 'justify-content-xl-start', 'align-items-xl-center');
                messageContent.classList.add('message');
                messageContent.style.marginLeft = '5px';
                messageContent.style.wordBreak = 'break-all';
                messageContent.style.fontSize = '14px';
                messageContent.innerText = receivedMessage.content;
                messageContent.id = 'messageContent' + receivedMessage.messageId;
                div6.appendChild(messageContent);
                div5.appendChild(div6);
                div4.appendChild(div5);
                div3.appendChild(div4);
                div7.classList.add('row');
                div8.classList.add('col', 'd-xl-flex', 'justify-content-xl-start');
                dateElement.classList.add('.date');
                dateElement.style.fontSize = '11px';
                dateElement.innerText = date.getDate() + '-' + month + ' ' + currentHours + ':' + currentMinutes;
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
                div1.classList.add('my_message');
                div1.id = 'message' + receivedMessage.messageId;
                div2.classList.add('col-sm-7', 'col-xl-6', 'offset-sm-3', 'offset-md-3', 'offset-lg-3', 'offset-xl-5');
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
                messageContent.classList.add('message', 'user_message');
                messageContent.style.marginRight = '0';
                messageContent.style.marginLeft = '0';
                messageContent.style.fontSize = '14px';
                messageContent.innerText = receivedMessage.content;
                messageContent.id = 'messageContent' + receivedMessage.messageId;
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
                dateElement.innerText = date.getDate() + '-' + month + ' ' + currentHours + ':' + currentMinutes;
                div7.appendChild(dateElement);
                div6.appendChild(div7);
                div8.classList.add('col-4', 'col-sm-2', 'col-lg-2', 'col-xl-1', 'offset-xl-0', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-center', 'align-items-sm-start');
                messageLink.setAttribute('href', "/user/" + receivedMessage.id);
                authorImg.classList.add('rounded-circle', 'd-xl-flex', 'justify-content-xl-center', 'align-items-xl-center');
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
            let editAndDeleteButtonsDiv = document.createElement('div');
            let anotherEditAndDeleteDiv = document.createElement('div');
            let deleteButton = document.createElement('button');
            let deleteIcon = document.createElement('i');
            let editButton = document.createElement('button');
            let editIcon = document.createElement('i');
            let date = new Date(receivedMessage.timestamp);
            let month = date.getMonth();
            let currentHours = date.getHours();
            let currentMinutes = date.getMinutes();
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
            let messageContent = document.createElement('img');
            month += 1;
            currentHours = ("0" + currentHours).slice(-2);
            currentMinutes = ("0" + currentMinutes).slice(-2);
            div1.classList.add('row');
            div1.style.padding = '10px';
            if (id !== receivedMessage.id) {
                div1.classList.add('opponents_message');
                div1.id = 'message' + receivedMessage.messageId;
                div2.classList.add('col-4', 'col-sm-2', 'col-lg-2', 'col-xl-1', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-center', 'align-items-sm-start');
                messageLink.setAttribute('href', "/user/" + receivedMessage.id);
                authorImg.classList.add('rounded-circle', 'd-xl-flex', 'justify-content-xl-center', 'align-items-xl-center');
                authorImg.src = '/media/avatars/avatar' + receivedMessage.id + '.png';
                authorImg.style.width = '50px';
                authorImg.style.height = '50px';
                messageLink.appendChild(authorImg);
                div2.appendChild(messageLink);
                div3.classList.add('col-sm-7', 'col-xl-6');
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
                div6.appendChild(messageContent);
                div5.appendChild(div6);
                div4.appendChild(div5);
                div3.appendChild(div4);
                div7.classList.add('row');
                div8.classList.add('col', 'd-xl-flex', 'justify-content-xl-start');
                dateElement.classList.add('.date');
                dateElement.style.fontSize = '11px';
                dateElement.innerText = date.getDate() + '-' + month + ' ' + currentHours + ':' + currentMinutes;
                div8.appendChild(dateElement);
                div7.appendChild(div8);
                div3.appendChild(div7);
                div1.appendChild(div2);
                div1.appendChild(div3);
                messageArea.appendChild(div1);
                chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
            } else {
                div1.classList.add('my_message');
                div1.id = 'message' + receivedMessage.messageId;
                div2.classList.add('col-sm-7', 'col-xl-6', 'offset-sm-3', 'offset-md-3', 'offset-lg-3', 'offset-xl-5');
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
                editButton.id = 'editMessage' + receivedMessage.messageId;
                deleteButton.classList.add('deleteButton');
                editButton.classList.add('editButton');
                deleteIcon.classList.add('fas', 'fa-times');
                editButton.style.border = 'none';
                editButton.style.background = 'none';
                editButton.style.paddingRight = '10px';
                editIcon.classList.add('fas', 'fa-pencil-alt');
                deleteButton.appendChild(deleteIcon);
                editButton.appendChild(editIcon);
                anotherEditAndDeleteDiv.appendChild(deleteButton);
                anotherEditAndDeleteDiv.appendChild(editButton);
                editAndDeleteButtonsDiv.appendChild(anotherEditAndDeleteDiv);
                div5.appendChild(messageContent);
                editAndDeleteButtonsDiv.appendChild(div5);
                div4.appendChild(editAndDeleteButtonsDiv);
                div3.appendChild(div4);
                div6.classList.add('row');
                div7.classList.add('col', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-end', 'justify-content-xl-end');
                dateElement.classList.add('.date');
                dateElement.style.fontSize = '11px';
                dateElement.innerText = date.getDate() + '-' + month + ' ' + currentHours + ':' + currentMinutes;
                div7.appendChild(dateElement);
                div6.appendChild(div7);
                div8.classList.add('col-4', 'col-sm-2', 'col-lg-2', 'col-xl-1', 'offset-xl-0', 'd-sm-flex', 'd-xl-flex', 'justify-content-sm-center', 'align-items-sm-start');
                messageLink.setAttribute('href', "/user/" + receivedMessage.id);
                messageLink.id = 'messageContent' + receivedMessage.messageId;
                authorImg.classList.add('rounded-circle', 'd-xl-flex', 'justify-content-xl-center', 'align-items-xl-center');
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


    $(document).on("click", '.deleteButton', function (event) {
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


    cancelEdit.addEventListener('click', function (event) {
        event.preventDefault();
        let fileButton = document.getElementById('fileLabel');
        let sendButton = document.getElementById('send');
        let editButton = document.getElementById('edit');
        let cancelEditButton = document.getElementById('cancelEdit');
        fileButton.style.display = 'inline-block';
        sendButton.style.display = 'inline-block';
        editButton.style.display = 'none';
        cancelEditButton.style.display = 'none';
        messageInput.value = '';
        currentEditMessageId = null;
    });


    sendButton.addEventListener('click', function (event) {
        sendMessage(event);
    });


    editButton.addEventListener('click', function (event) {
        sendEditedMessage(event);
    });


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
            chatTitle.textContent = editChatTitleInput.value;
            let chatListElem = document.getElementById('chatTitle' + roomId.textContent);
            chatListElem.textContent = editChatTitleInput.value;
        }
        chatTitle.style.display = 'inline-block';
        editChatTitleButton.style.display = 'inline-block';
        editChatTitleInput.style.display = 'none';
        confirmEditChatTitle.style.display = 'none';
    });
});