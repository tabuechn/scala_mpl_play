$('document').ready(function(){
    console.log("starting battleship game");
    start();
});
var socket;
var orientation = "v";
var shipSize = 1;
var fieldSize = 10;
var currentPlayer = {};
function start() {
    socket = new WebSocket("ws://localhost:9000/socket");
    socket.onmessage = function (event) {
        var data = event.data;
        var obj = JSON.parse(data);
        console.log("got message:");
        console.log(obj);
        updateGame(obj);
    };
    socket.onopen = function(event) {
        console.log("socket opened");
        //socket.send(JSON.stringify("{\"testMessage\": \"test\" }"));
    };
    socket.onclose = function(event) {
        console.log("socket closed");
        console.log(event);
        alert("your socket has been closed!");
    };
    socket.onerror = function(event) {
        console.log("got socket error");
    };
    setSelectedShip();
    setVertical();
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
    setPlayerColor(currentPlayer.color);
    setPlayerShips(currentPlayer.shipInventory);
    fieldSize = currentPlayer.fieldSize;
    drawField(currentPlayer.field);
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
        currentField.push([]);
        gameContainer.append("<div class='row'></div>")
    }
    var rowCounter = 0;
    var colCounter = 0;
    for(var key in field) {
        if(colCounter == (fieldSize)) {
            rowCounter++;
            colCounter = 0;
        }
        var currentRow = gameContainer.children()[rowCounter];
        // todo: checkt wtf is going on here
        if(currentRow !== undefined) {
            var hasShip = field[key];
            var cords = key.split(" ");
            var x = cords[0];
            var y = cords[1];
            var html;

            if(hasShip) {
                html = "<button class=\"field-card mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent\" onclick='buttonClick(" + x + "," + y +")'>S</button>"
            } else {
                html = "<button class=\"field-card mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent\" onclick='buttonClick(" + x + "," + y +")'>" + x + " " + y + "</button>"
            }
            currentRow.innerHTML += html;
            colCounter++;
        } else {
            console.log("got undifned stuff");
        }
        //console.log(currentRow);


    }
}

function buttonClick(x,y) {
    console.log("got click on x:" + x + " y:" + y);
    $.get("/setShips/" + x + "/" + y + "/" + shipSize + "/" + orientation + "/" + currentPlayer.color)
}

function clearField() {
    $(".gameContainer").empty();
}