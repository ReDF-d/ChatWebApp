let profileForm = document.getElementById("profile-form");
let profileButton = document.getElementById("profile-button");
let adminEdit = document.getElementById("admin-edit");
let addFriend = document.getElementById("add-friend");
let addFriendForm = document.getElementById("add-friend-form");
let sendMessageForm = document.getElementById("send-message-form");
let chatButton = document.getElementById("button-chat");


if (profileButton != null)
    profileButton.addEventListener("click", function () {
        profileForm.submit();
    });

if (addFriend != null)
    addFriend.addEventListener("click", function () {
        addFriendForm.submit();
    });

if (adminEdit != null)
    adminEdit.addEventListener("click", function () {
        let path = window.location.pathname.split('/');
        window.location.href = '/adminpanel/edituser/' + path[path.length - 1];
    });

if (chatButton != null) {
    chatButton.addEventListener("click", function () {
        sendMessageForm.submit();
    })
}
