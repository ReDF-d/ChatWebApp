'use strict';

let createButton = document.getElementById('createChat');
let createChatForm = document.getElementById('new_chat_form');
let formToHide = document.getElementById('chats_body');
let closeButton = document.getElementById('closeCreate');


if (createButton != null)
    createButton.addEventListener("click", function () {
        formToHide.style.display = 'none';
        createChatForm.style.display = 'inline';
    });
if (closeButton != null)
    closeButton.addEventListener("click", function () {
        formToHide.style.display = 'inline';
        createChatForm.style.display = 'none';
    });