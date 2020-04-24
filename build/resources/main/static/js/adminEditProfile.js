'use strict';

let markBanned = document.getElementById('markBanned');
let banDateLabel = document.getElementById('banDateLabel');
let banDate = document.getElementById('banDate');

function handleChange() {
    if (markBanned.checked) {
        banDateLabel.hidden = false;
        banDate.hidden = false;
    } else {
        banDateLabel.hidden = true;
        banDate.hidden = true;
    }
}

