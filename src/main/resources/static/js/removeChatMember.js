'use strict';

let removeMembersForm = document.getElementById('removeMembersForm');
let closeRemoveMembersButton = document.getElementById('closeRemoveMembers');


$(document).on("click", '#removeMembers', function () {
    formToHide.style.display = 'none';
    removeMembersForm.style.display = 'inline';
});

if (closeRemoveMembersButton != null)
    closeRemoveMembersButton.addEventListener("click", function () {
        formToHide.style.display = 'inline';
        removeMembersForm.style.display = 'none';
    });


$(document).on("click", '.removeMemberButton', function (event) {
    event.preventDefault();
    let memberId = event.currentTarget.id.match(/\d+/g);
    let path = window.location.pathname.split('/');
    let room = path[path.length - 1];
    let form_data = new FormData();
    form_data.append('memberId', memberId);
    form_data.append('roomId', room);
    let token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: "/removeChatMember/",
        type: "POST",
        headers: {"X-CSRF-TOKEN": token},
        data: form_data,
        processData: false,
        contentType: false,
        cache: false,
        success: function () {
            document.getElementById('membersList').removeChild(document.getElementById('removeMember' + memberId));
            let onlineElem = document.getElementById('online' + memberId);
            let offlineElem = document.getElementById('offline' + memberId);
            if (onlineElem !== null)
                document.getElementById('onlineUsers').removeChild(onlineElem);
            if (offlineElem !== null)
                document.getElementById('offlineUsers').removeChild(offlineElem);
        }
    });
});