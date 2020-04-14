$(document).ready(function () {
    'use strict';
    let messageInput = document.querySelector('#message');
    let messageArea = document.querySelector('#messageArea');
    let messageForm = document.querySelector('#messageSendForm');
    let stompClient = null;
    let username = null;
    let id = null;
    let date = new Date();
    let login = null;
    let roomId = document.querySelector('#roomId');
    let connectingError = document.createElement('div');
    let chatWindow = $("#chatWindow");

    connectingError.classList.add('hidden', 'text-danger', 'text-center');
    connectingError.id = 'connectionError';
    chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
    if (!stompClient)
        connect();
    messageForm.addEventListener('submit', sendMessage, true);


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


    function onConnected() {
        stompClient.subscribe('/topic/chat/' + roomId.textContent, onMessageReceived);
        stompClient.subscribe('/topic/onlineTracker', onStatusChange);
        stompClient.send("/app/chat.addUser." + roomId.textContent,
            {},
            JSON.stringify({sender: username, type: 'JOIN'})
        );
    }


    function onError(error) {
        connectingError.style.visibility = "visible";
        connectingError.textContent = 'Невозможно подключится к серверу, попробуйте позже';
        messageArea.appendChild(connectingError);
    }


    function onStatusChange(payload) {
        let statusChangeMessage = JSON.parse(payload.body);
        if (statusChangeMessage.status === 'ONLINE')
            alert('online');
        else if (statusChangeMessage.status === 'OFFLINE')
            alert('offline');
    }


    function sendMessage(event) {
        event.preventDefault();
        let messageContent = messageInput.value.trim();
        if (messageContent && stompClient) {
            let chatMessage = {
                roomId: roomId.textContent,
                id: id,
                sender: username,
                login: login,
                content: messageInput.value,
                type: 'CHAT',
                timestamp: date
            };
            stompClient.send("/app/chat.sendMessage." + roomId.textContent, {}, JSON.stringify(chatMessage));
            stompClient.send("/app/chat.saveMessage." + roomId.textContent, {}, JSON.stringify(chatMessage));
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
                div6.classList.add('d-xl-flex', 'justify-content-xl-center', 'align-items-xl-center');
                messageContent.classList.add('message');
                messageContent.style.marginLeft = '5px';
                messageContent.style.width = '100%';
                messageContent.style.fontSize = '14px';
                messageContent.innerText = receivedMessage.content;
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
                div1.appendChild(div2);
                div1.appendChild(div3);
                div1.appendChild(div7);
                messageArea.appendChild(div1);
                chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
            } else {
                div1.classList.add('my_message');
                div2.classList.add('col-sm-7', 'col-xl-6', 'offset-sm-3', 'offset-md-3', 'offset-lg-3', 'offset-xl-5');
                div3.classList.add('row');
                div4.classList.add('col-sm-12', 'col-xl-11', 'offset-xl-1');
                div4.style.padding = '5px';
                div5.classList.add('d-sm-flex', 'd-md-flex', 'd-lg-flex', 'd-xl-flex', 'justify-content-sm-end', 'justify-content-md-end', 'justify-content-lg-end', 'justify-content-xl-end', 'align-items-xl-center');
                messageContent.classList.add('message', 'user_message');
                messageContent.style.marginRight = '0';
                messageContent.style.marginLeft = '0';
                messageContent.style.fontSize = '14px';
                messageContent.innerText = receivedMessage.content;
                div5.appendChild(messageContent);
                div4.appendChild(div5);
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
                div6.classList.add('d-xl-flex', 'justify-content-xl-center', 'align-items-xl-center');
                messageContent.classList.add('message');
                messageContent.style.marginLeft = '5px';
                messageContent.style.width = '100%';
                messageContent.style.fontSize = '14px';
                messageContent.src = receivedMessage.content.substr(1);
                messageContent.style.maxWidth = '300px';
                messageContent.style.maxHeight = '300px';
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
                div1.appendChild(div2);
                div1.appendChild(div3);
                div1.appendChild(div7);
                messageArea.appendChild(div1);
                chatWindow.animate({scrollTop: chatWindow[0].scrollHeight}, 10);
            } else {
                div1.classList.add('my_message');
                div2.classList.add('col-sm-7', 'col-xl-6', 'offset-sm-3', 'offset-md-3', 'offset-lg-3', 'offset-xl-5');
                div3.classList.add('row');
                div4.classList.add('col-sm-12', 'col-xl-11', 'offset-xl-1');
                div4.style.padding = '5px';
                div5.classList.add('d-sm-flex', 'd-md-flex', 'd-lg-flex', 'd-xl-flex', 'justify-content-sm-end', 'justify-content-md-end', 'justify-content-lg-end', 'justify-content-xl-end', 'align-items-xl-center');
                messageContent.classList.add('message', 'user_message');
                messageContent.style.marginRight = '0';
                messageContent.style.marginLeft = '0';
                messageContent.style.fontSize = '14px';
                messageContent.src = receivedMessage.content.substr(1);
                messageContent.style.maxWidth = '300px';
                messageContent.style.maxHeight = '300px';
                div5.appendChild(messageContent);
                div4.appendChild(div5);
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
        }
    }


    window.addEventListener('paste', e => {
        alert('caught');
        document.getElementById('file').files = e.clipboardData.files;
    });
});