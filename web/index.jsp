<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="en">
<head>
    <title>Chat Room</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="style.css" type="text/css"/>
    <script src="chat.js"></script>
</head>
<body onload="connect();" onunload="disconnect();">
<h1> Chat Room </h1>
<textarea id="messages" class="panel message-area" disabled></textarea>
<div class="panel input-area">
    <input id="userName" class="text-field" type="text" placeholder="Name"/>
    <input id="messageInput" class="text-field" type="text" placeholder="Message"
           onkeydown="if (event.keyCode === 13)
               sendMessage();"/>
    <input class="button" type="submit" value="Send" onclick="sendMessage();"/>
</div>
</body>
</html>
