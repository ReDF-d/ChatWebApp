$(document).ready(function () {
    let onlineTrackerClient = null;
    let principalId = null;
    let username = document.getElementById('username');
    if (document.getElementById('id') != null)
        principalId = document.getElementById('id').textContent.trim();
    if (principalId) {
        if (!onlineTrackerClient)
            connectOnlineTracker();
    }


    function connectOnlineTracker() {
        let onlineTrackerSocket = new SockJS('http://localhost:8080/notificationHub');
        onlineTrackerClient = Stomp.over(onlineTrackerSocket);
        onlineTrackerClient.debug = true;
        onlineTrackerClient.connect({}, onOnlineTrackerConnected(), onOnlineTrackerError());
    }


    async function onOnlineTrackerConnected() {
        await sleep(1000);
        let message = {
            id: principalId,
            username: username.innerText,
            status: 'ONLINE'
        };
        onlineTrackerClient.send("/app/onlineTracker.saveUserStatus", {}, JSON.stringify(message));
        onlineTrackerClient.send("/app/onlineTracker.userConnected", {}, JSON.stringify(message));
    }


    function onOnlineTrackerError(error) {

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
            onlineTrackerClient.send("/app/onlineTracker.saveUserStatus", {}, JSON.stringify(message));
            onlineTrackerClient.send("/app/onlineTracker.userDisconnected", {}, JSON.stringify(message));
        });
    }
});