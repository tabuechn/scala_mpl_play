package controllers

import akka.actor.{Actor, ActorRef, Props}
import com.fasterxml.jackson.databind.ObjectMapper
import de.htwg.se.battleship.model.Message._
import de.htwg.se.battleship.model.{Field, Player, Point}
import play.api.libs.json._

import scala.collection.mutable

object WebSocketActor {
  def props(webSocketOut: ActorRef, controllerActor: ActorRef) =
    Props(new WebSocketActor(webSocketOut, controllerActor))
}

class WebSocketActor(socketOut: ActorRef, controller: ActorRef) extends Actor{

  controller ! RegisterObserver

  var currentPlayer: Player = _

  override def receive: Receive = {
    case StartGame() => println("Starting game")
    case PrintMessage(message:String) =>  {
      println(message)
      socketOut ! createMessageJson(message)
    }
    case Update(state: Phase, activePlayer: Player, otherPlayer: Player) =>  {
      println(" got update message")
      currentPlayer = activePlayer
      socketOut ! getJsonForUpdate(Update(state,activePlayer,otherPlayer))
    }
    case _ => println("got unknown message");
  }

  private def createMessageJson(message:String): JsValue = {
    Json.obj(
      "type" -> "message",
      "message" -> message
    )
  }

  private def getJsonForUpdate(update: Update) : JsValue = {
    Json.obj(
      "type" -> "update",
      "activePlayer" -> getJsonForPlayer(update.activePlayer),
      "otherPlayer" -> getJsonForPlayer(update.otherPlayer),
      "state" -> update.state.toString()
    )
  }

  private def getJsonForPlayer(player: Player): JsValue = {
    Json.obj(
      "color" -> player.COLOR,
      "shipInventory" -> getJsonForShips(player.shipInventory),
      "field" -> getJsonForField(player.field),
      "fieldSize" -> player.field.size
    )
  }

  private def getJsonForField(field: Field): JsValue = {
    var jsonString = "{"
    val keys = field.fieldGrid.keys
    keys.foreach((point) => {
       val pointString = "\"" + point.x + " " + point.y + "\""
      jsonString += pointString + ": " + field.hasShip(point) + ","
    })
    /*
    for( x <- 0 to field.size - 1) {
      for( y <- 0 to field.size - 1) {
        val pointString = "\"" + x + " " + y + "\""
        jsonString += pointString + ": " + field.hasShip(Point(x,y)).toString() + ","
      }
    }*/
    if(jsonString.length() > 1) {
      jsonString = jsonString.dropRight(1)
    }
    jsonString += "}"
    Json.parse(jsonString)
  }

  private def getJsonForShips(ships: mutable.Map[Int,Int]): JsValue = {
    val keys = ships.keys
    var jsonString = "{"
    for( ship <- keys ) {
      jsonString += "\"" + ship + "\"" + ": " + show(ships.get(ship)) + ","
    }
    if(jsonString.length > 1) {
      jsonString = jsonString.dropRight(1)
    }
    jsonString += "}"
    Json.parse(jsonString)
  }

  def show(x: Option[Int]) = x match {
    case Some(s) => s
    case None => 0
  }

}
