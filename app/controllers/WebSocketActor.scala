package controllers

import akka.actor.{Actor, ActorRef, Props}
import de.htwg.se.battleship.model.Message._
import de.htwg.se.battleship.model.{Field, Player}
import play.api.libs.json._

object WebSocketActor {
  def props(webSocketOut: ActorRef, controllerActor: ActorRef) =
    Props(new WebSocketActor(webSocketOut, controllerActor))
}

class WebSocketActor(socketOut: ActorRef, controller: ActorRef) extends Actor{

  controller ! RegisterObserver

  override def receive: Receive = {
    case StartGame() => println("Starting game")
    case PrintMessage(message:String) => println(message)
    case Update(state: Phase, activePlayer: Player, otherPlayer: Player) =>  {
      println(" got update message")
      socketOut ! getJsonForUpdate(Update(state,activePlayer,otherPlayer))
    }
    case _ => println("got unknown message")
  }

  private def getJsonForUpdate(update: Update) : JsValue = {
    Json.obj(
      "activePlayer" -> getJsonForPlayer(update.activePlayer),
      "otherPlayer" -> getJsonForPlayer(update.otherPlayer)
    )
  }

  private def getJsonForPlayer(player: Player): JsValue = {
    Json.obj(
      "color" -> player.COLOR,
      "ships" -> player.shipInventory.toString(),
      "field" -> player.field.toString()
    )
  }

  private def getJsonForField(field: Field): JsValue = {
    Json.obj()

  }
}
