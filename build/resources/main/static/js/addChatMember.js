'use strict';

let addButton = document.getElementById('addMember');
let addForm = document.getElementById('add_user_form');
let chatToHide = document.getElementById('chats_body');
let closeAddButton = document.getElementById('closeAdd');


if (addButton != null)
    addButton.addEventListener("click", function () {
        chatToHide.style.display = 'none';
        addForm.style.display = 'inline';
    });

if (closeAddButton != null)
    closeAddButton.addEventListener("click", function () {
        chatToHide.style.display = 'inline';
        addForm.style.display = 'none';
    });