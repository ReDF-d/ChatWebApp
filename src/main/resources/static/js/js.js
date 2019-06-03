window.onload = changeBG;

function changeBG() {
    var first = 1;
    var last = 7;
    var path = 'img/';

    var img_src = 'url("' + path + getRandomInt(first, last) + '.jpg")';
    var div = document.getElementById("main");
    div.style.backgroundImage = img_src;
}

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
