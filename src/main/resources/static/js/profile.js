'use strict';
$(document).ready(function () {
    let adminEdit = document.getElementById("admin-edit");
    let defaultStatus = document.getElementById('defaultStatus');
    let status = document.getElementById('status');
    let input = document.getElementById('changeStatusInput');
    let confirm = document.getElementById('confirmEditStatus');


    if (adminEdit != null)
        adminEdit.addEventListener("click", function () {
            let path = window.location.pathname.split('/');
            window.location.href = '/adminpanel/edituser/' + path[path.length - 1];
        });


    $('#editStatusButton').on('click', function () {
        if (typeof defaultStatus !== 'undefined' && defaultStatus !== null)
            defaultStatus.style.display = 'none';
        if (typeof status !== 'undefined' && status !== null)
            status.style.display = 'none';
        input.style.display = 'inline';
        confirm.style.display = 'inline';
    });


    $('#confirmEditStatus').on('click', async function () {
        let changeStatusData = new FormData();
        changeStatusData.append('status', input.value);
        changeStatusData.append('userId', $('#id').text());
        let csrfToken = $("meta[name='_csrf']").attr("content");
        $.ajax({
            url: "/user/edit",
            type: "POST",
            headers: {"X-CSRF-TOKEN": csrfToken},
            data: changeStatusData,
            processData: false,
            contentType: false,
            cache: false,
        });
        await sleep(100);
        if (defaultStatus !== null)
            defaultStatus.style.display = 'inline';
        if (status !== null)
            status.style.display = 'inline';
        input.style.display = 'none';
        input.value = '';
        confirm.style.display = 'none';
        window.location.replace('/user/' + $('#id').text());
    });


    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }
});

