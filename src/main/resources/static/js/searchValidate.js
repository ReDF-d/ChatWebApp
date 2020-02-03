'use strict';

let searchUserForm = document.getElementById('searchUser');
let searchUserInput = document.getElementById('searchUserInput');
let searchUserSubmit = document.getElementById('searchUserSubmit');


if (searchUserForm != null) {
    searchUserSubmit.addEventListener('click', function (event) {
        if (searchUserInput.value === null || searchUserInput.value.trim() === '') {
            searchUserInput.classList.add('invalid-search');
            searchUserInput.placeholder = 'Пустой запрос';
            event.preventDefault();
        } else searchUserForm.submit();
    });
}
