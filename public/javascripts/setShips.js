var firstSelect = false;

var orientation = "v";

$('document').ready(function(){
    console.log("starting setting ships");
    start();
});

function start() {
    setListeners();
    setVertical();
}

function setListeners() {
    $("#verticalButton").click(function() {setVertical()});
    $("#horizontalButton").click(function() {setHorizontal()});
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

function fieldClick () {
    
}