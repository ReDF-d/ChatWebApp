$(document).ready(function () {
    let notificationHubClient = null;
    let principalId = null;
    let username = document.getElementById('username');
    if (document.getElementById('id') != null)
        principalId = document.getElementById('id').textContent.trim();
    if (principalId) {
        if (!notificationHubClient)
            connectNotificationHub()
    }


    function connectNotificationHub() {
        let onlineTrackerSocket = new SockJS('http://' + window.location.host + '/notificationHub');
        notificationHubClient = Stomp.over(onlineTrackerSocket);
        notificationHubClient.debug = true;
        notificationHubClient.connect({}, onNotificationHubConnected, onNotificationHubError);
    }


    async function onNotificationHubConnected() {
        await sleep(1000);
        let message = {
            id: principalId,
            username: username.innerText,
            status: 'ONLINE'
        };
        notificationHubClient.send("/app/onlineTracker.saveUserStatus", {}, JSON.stringify(message));
        notificationHubClient.send("/app/onlineTracker.userConnected", {}, JSON.stringify(message));
        notificationHubClient.subscribe("/topic/friendNotification", onFriendNotification);
    }


    function onFriendNotification(payload) {
        let notification = JSON.parse(payload.body);
        if (principalId === notification.recipient) {
            let notificationDiv = document.createElement('div');
            notificationDiv.classList.add('top-left', 'notify', 'do-show');
            notificationDiv.style.backgroundColor = '#66BB6A';
            let notificationSpan = document.createElement('span');
            if (notification.status === 'pending')
                notificationSpan.innerText = notification.senderUsername + ' хочет добавить вас в друзья';
            else
                notificationSpan.innerText = notification.senderUsername + ' принял вашу заявку в друзья';
            notificationDiv.appendChild(notificationSpan);
            document.body.appendChild(notificationDiv);
        }
    }


    function onNotificationHubError(error) {
        console.log(error);
    }


    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }


    if (principalId) {
        $(window).on("beforeunload", function () {
            let message = {
                id: principalId,
                username: username.innerText,
                status: 'OFFLINE'
            };
            notificationHubClient.send("/app/onlineTracker.saveUserStatus", {}, JSON.stringify(message));
            notificationHubClient.send("/app/onlineTracker.userDisconnected", {}, JSON.stringify(message));
        });
    }
});