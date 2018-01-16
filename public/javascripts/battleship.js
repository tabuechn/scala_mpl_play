$('document').ready(function(){
    console.log("starting battleship game");
    start();
});
var socket;
var orientation = "v";
var shipSize = 1;
var fieldSize = 10;
var currentPlayer = {};
var otherPlayer = {};
var state = "init";
function start() {
    socket = new WebSocket("ws://localhost:9000/socket");
    socket.onmessage = function (event) {
        var data = event.data;
        var json = JSON.parse(data);
        console.log(json);
        messageHandler(json);
    };
    socket.onopen = function(event) {
        console.log("socket opened");
    };
    socket.onclose = function(event) {
        console.log("socket closed");
        start()
    };
    socket.onerror = function(event) {
        console.log("got socket error");
    };
    setSelectedShip();
    setVertical();
}

function messageHandler(json) {
    switch (json.type) {
        case "update":
            updateGame(json);
            break;
        case "message":
            onMessageRecive(json);
            break;
        default:
            console.log("got unknown message");
    }
}

function onMessageRecive(json) {
    alert(json.message);
}

function setShipSize(size) {
    shipSize = size;
    setSelectedShip();
    console.log("ship size is now " + shipSize);
}

function setSelectedShip() {
    for(var i = 1; i < 6; i++) {
        var shipSelector = $("#ships" + i);
        shipSelector.css('background-color', 'lightblue');
        shipSelector.css('color', 'black');
    }
    var selectedShipContainer = $("#ships" + shipSize);
    selectedShipContainer.css('background-color', 'red');
    selectedShipContainer.css('color', 'white');
}

function setVertical() {
    resetOrientationButtons();
    $("#verticalButton").addClass("mdl-button--colored");
    orientation = "v";
}

function setHorizontal() {
    resetOrientationButtons();
    $("#horizontalButton").addClass("mdl-button--colored");
    orientation = "h";
}

function resetOrientationButtons() {
    $("#verticalButton").removeClass("mdl-button--colored");
    $("#horizontalButton").removeClass("mdl-button--colored")
}

function updateGame(json) {
    currentPlayer = json.activePlayer;
    otherPlayer = json.otherPlayer;
    state = json.state;
    if(state === "AnnounceWinner") {
        alert(currentPlayer.color + " has won! restarting the game");
        window.location = "http://localhost:9000/restart"
    }
    setInfoText();
    setPlayerColor(currentPlayer.color);
    setPlayerShips(currentPlayer.shipInventory);
    fieldSize = currentPlayer.fieldSize;
    drawField(currentPlayer.field);
}

function setInfoText() {
    $("#statusText").html(state + "&nbsp;")
}

function setPlayerColor(color) {
    $("#playerColor").html(color);
}

function setPlayerShips(shipsMap) {
    for(var i = 1; i < 6; i++) {
        var numberForValue = shipsMap[i];
        if(numberForValue === undefined) {numberForValue = 0}
        $("#ships" + i).html(i + ": " + numberForValue);
    }
}

function drawField(field) {
    var gameContainer = $(".gameContainer");
    gameContainer.empty();
    clearField();
    var currentField = [];
    for(var rows = 0; rows < fieldSize; rows++) {
        gameContainer.append("<div class='row'></div>");
        var row = gameContainer.children()[rows];
        for(var cols = 0; cols < fieldSize; cols++) {
            row.innerHTML += "<button class=\"field-card mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent\" onclick='buttonClick(" + (cols+1) + "," + (rows+1) +")'></button>"
        }
    }
    for(var key in field) {
        var cords = key.split(" ");
        var x = cords[0] - 1;
        var y = cords[1] - 1;

        var row = gameContainer.children()[y];
        var child = $(row).children()[x];
        child.innerHTML = "S";
    }
}

function buttonClick(x,y) {
    console.log("got click on x:" + x + " y:" + y);
    if(state === "PlaceShipTurn") {
        $.get("/setShips/" + x + "/" + y + "/" + shipSize + "/" + orientation + "/" + currentPlayer.color)
    } else if( state === "ShootTurn") {
        $.get("/shootShip/" + x + "/" + y + "/" + otherPlayer.color)
    }

}

function clearField() {
    $(".gameContainer").empty();
}