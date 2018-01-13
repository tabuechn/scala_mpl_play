$('document').ready(function(){
    console.log("starting battleship game");
    start();
});
var socket;
function start() {
    var socket = new WebSocket("ws://localhost:9000/socket");
    socket.onmessage = function (event) {
        console.log("got message:");
        console.log(event);
    };
    socket.onopen = function(event) {
        console.log("socket opened");
    };
    socket.onclose = function(event) {
        console.log("socket closed");
    };
    socket.onerror = function(event) {
        console.log("got socket error");
    }
}