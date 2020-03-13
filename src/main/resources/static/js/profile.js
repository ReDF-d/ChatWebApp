let adminEdit = document.getElementById("admin-edit");

if (adminEdit != null)
    adminEdit.addEventListener("click", function () {
        let path = window.location.pathname.split('/');
        window.location.href = '/adminpanel/edituser/' + path[path.length - 1];
    });

