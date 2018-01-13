$('document').ready(function(){
    console.log("starting battleship game");
    start();
});
var socket;
var orientation = "v";
var shipSize = 1;
var fieldSize = 10;
function start() {
    var socket = new WebSocket("ws://localhost:9000/socket");
    socket.onmessage = function (event) {
        var data = event.data;
        var obj = JSON.parse(data);
        console.log("got message:");
        console.log(obj);
        updateGame(obj);
    };
    socket.onopen = function(event) {
        console.log("socket opened");
    };
    socket.onclose = function(event) {
        console.log("socket closed");
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
        shipSelector.css('background-color', 'white');
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
    var player = json.activePlayer;
    setPlayerColor(player.color);
    setPlayerShips(player.shipInventory);
    fieldSize = player.fieldSize;
    drawField(player.field);
}

function setPlayerColor(color) {
    $("#playerColor").html(color);
}

function setPlayerShips(shipsMap) {
    console.log(shipsMap);
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
    console.log(field);
    var currentField = [];
    for(var rows = 0; rows < fieldSize; rows++) {
        currentField.push([]);
        gameContainer.append("<div class='row'></div>")
    }
    var rowCounter = 0;
    var colCounter = 0;
    for(var key in field) {
        if(colCounter == fieldSize) {
            rowCounter++;
            colCounter = 0;
            continue;
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
                html = "<button class=\"field-card mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent\">S</button>"
            } else {
                html = "<button class=\"field-card mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent\"> </button>"
            }
            currentRow.innerHTML += html;
            colCounter++;
        }
        //console.log(currentRow);


    }
}

function clearField() {
    $(".gameContainer").empty();
}