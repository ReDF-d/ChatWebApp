'use strict';

let selectedRoomId;
let selectedRoomIdParsedNumber;
let confirmChatExit = document.getElementById('confirmChatExitForm');
let chatExitConfirmButton = document.getElementById('chatExitConfirmButton');
let chatExitRefuseButton = document.getElementById('chatExitRefuseButton');

function openExitConfirm(e, id) {
    e.preventDefault();
    selectedRoomId = id;
    selectedRoomIdParsedNumber = selectedRoomId.match(/\d+/g);
    formToHide.style.display = 'none';
    confirmChatExit.style.display = 'inline';
}


if (chatExitRefuseButton != null) {
    chatExitRefuseButton.addEventListener('click', function () {
        formToHide.style.display = 'inline';
        confirmChatExit.style.display = 'none';
    })
}


if (chatExitConfirmButton != null) {
    chatExitConfirmButton.addEventListener('click', async function () {
        let exitChatData = new FormData();
        exitChatData.append('userId', $('#id').text());
        exitChatData.append('roomId', selectedRoomId);
        let csrfToken = $("meta[name='_csrf']").attr("content");
        $.ajax({
            url: "/chat/" + selectedRoomIdParsedNumber,
            type: "POST",
            headers: {"X-CSRF-TOKEN": csrfToken},
            data: exitChatData,
            processData: false,
            contentType: false,
            cache: false,
        });
        await sleep(100);
        window.location.replace('/chats');
    })
}


function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}