'use strict';

let first = 1;
let last = 24;
let path = '/images/';
let div = document.getElementById("main");

if (div.classList.contains("terms"))
    div.style.backgroundImage = 'url("' + path + 9 + '.jpg")';
else if (div.classList.contains("chat"))
    div.style.backgroundImage = 'url("' + path + 13 + '.jpg")';
else {
    div.style.backgroundImage = 'url("' + path + getRandomInt(first, last) + '.jpg")';
}

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
