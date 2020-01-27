'use strict';
let messageInput = document.querySelector('#message');
let messageArea = document.querySelector('#messageArea');
let messageForm = document.querySelector('#messageSendForm');
let stompClient = null;
let username = null;
let id = null;
let date = new Date();
let login = false;
let roomId = document.querySelector('#roomId');
let connectingError = document.createElement('div');
connectingError.classList.add('hidden', 'chat-message');
connectingError.id = 'connectionError';
messageArea.scrollTop = messageArea.scrollHeight;
if (!stompClient)
    connect();
messageForm.addEventListener('submit', sendMessage, true);


function connect() {
    id = document.querySelector('#id').textContent.trim();
    username = document.querySelector('#username').innerText.trim();
    login = document.querySelector('#login').textContent.trim();
    if (username) {
        let socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
        // stompClient.debug = null
    }
}


function onConnected() {
    stompClient.subscribe('/topic/chat/' + roomId.textContent, onMessageReceived);
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


function sendMessage(event) {
    let messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        let chatMessage = {
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
    event.preventDefault();
}


function onMessageReceived(payload) {
    let receivedMessage = JSON.parse(payload.body);
    let messageDiv = document.createElement('div');
    if (id !== receivedMessage.id)
        messageDiv.classList.add('chat-message');
    else
        messageDiv.classList.add('mychat-message');
    let messageAuthor = document.createElement('a');
    let messageContent = document.createElement('span');
    let date = new Date(receivedMessage.timestamp);
    let month = date.getMonth();
    month += 1;
    let dateElement = document.createElement('div');
    let currentHours = date.getHours();
    currentHours = ("0" + currentHours).slice(-2);
    let currentMinutes = date.getMinutes();
    currentMinutes = ("0" + currentMinutes).slice(-2);
    dateElement.innerText = date.getDate() + '-' + month + ' ' + currentHours + ':' + currentMinutes;
    dateElement.classList.add('time');
    if (receivedMessage.type === 'JOIN') {

    } else if (receivedMessage.type === 'LEAVE') {

    } else {
        if (id !== receivedMessage.id) {
            messageAuthor.innerText = receivedMessage.sender;
            messageAuthor.setAttribute('href', "/user/" + receivedMessage.id);
            messageDiv.appendChild(messageAuthor);
        }

        messageContent.innerText = receivedMessage.content;
        messageDiv.appendChild(messageContent);
        messageDiv.appendChild(dateElement);
        if (id === receivedMessage.id) {
            let editDelete = document.createElement('div');
            editDelete.classList.add('editanddeletebuttons');
            messageDiv.appendChild(editDelete);
        }
        messageArea.appendChild(messageDiv);
        messageArea.appendChild(document.createElement('br'));
        messageArea.scrollTop = messageArea.scrollHeight;
    }
}