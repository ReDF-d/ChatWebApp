'use strict';
var messageInput;
var messageArea ;
var connectingElement ;
var messageForm;
var stompClient = null;
var username = null;
window.onload = function () {
    messageInput = document.querySelector('#message');
    messageArea = document.querySelector('#messageArea');
    connectingElement = document.querySelector('.connecting');
    messageForm = document.querySelector('#messageForm');
    if (!stompClient)
        connect();
    messageForm.addEventListener('submit', sendMessage, true);
};

    function connect() {
        username = document.querySelector('#username').innerText.trim();
        if (username) {
            var socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, onConnected, onError);
        }
    }

    function onConnected() {
        stompClient.subscribe('/topic/public', onMessageReceived);
        stompClient.send("/app/chat.addUser",
            {},
            JSON.stringify({sender: username, type: 'JOIN'})
        );
        connectingElement.classList.add('hidden');
    }

    function onError(error) {
        connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
        connectingElement.style.color = 'red';
    }

    function sendMessage(event) {
        var messageContent = messageInput.value.trim();
        if (messageContent && stompClient) {
            var chatMessage = {
                sender: username,
                content: messageInput.value,
                type: 'CHAT'
            };
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            messageInput.value = '';
        }
        event.preventDefault();
    }

    function onMessageReceived(payload) {
        var message = JSON.parse(payload.body);
        var messageElement = document.createElement('li');
        if (message.type === 'JOIN') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' подключился к чату';
        } else if (message.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' покинул чат';
        } else {
            messageElement.classList.add('chat-message');
            var usernameElement = document.createElement('span');
            var usernameText = document.createTextNode(message.sender);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
        }
        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    }