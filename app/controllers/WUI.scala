package controllers

import akka.actor.ActorRef
import de.htwg.se.battleship.controller.Controller
import de.htwg.se.battleship.model.Message.{Phase, PrintMessage, RegisterObserver, Update}
import de.htwg.se.battleship.model.{Field, Orientation, Player, Point}
import de.htwg.se.battleship.view.View
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket

class WUI(val controller: ActorRef) extends View{

  controller ! RegisterObserver

  override def receive: Receive = {
    case Update(state: Phase, activePlayer: Player, otherPlayer: Player) => println("updateing with state")
    case PrintMessage(message: String) => println(Console.BLACK + message)
  }

  override def playerSwitch(player: Player): Unit =  {
    println("switching player")
  }

  override def printField(field: Field, color: String): Unit = {
    println("printing field")
  }

  override def printMessage(message: String): Unit = {
    println(message)
  }

  override def selectShip(player: Player): Int = {
    println("selecting ship of: " + player.COLOR)
    1
  }

  override def readPoint(): Point = {
    println("reading Point")
    Point(0,0)
  }

  override def readOrientation(): Orientation = {
    println("reading orientation")
    Orientation.HORIZONTAL
  }

  override def announceWinner(winner: Player): Unit =  {
    println(winner.COLOR + " wins!")
  }

  override def shootTurn(enemy: Player): Unit = {
    println(enemy.COLOR + " shoot turn")
  }

}
