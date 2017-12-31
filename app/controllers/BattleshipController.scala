package controllers

import javax.inject.{Inject, Singleton}

import de.htwg.se.battleship.controller.Controller
import de.htwg.se.battleship.view.{TuiView, View}
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.mvc._
import akka.stream.scaladsl._
import de.htwg.se.battleship.model.{Field, Orientation, Player, Point}

@Singleton
class BattleshipController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with View {

  var controller: Controller = Controller(10, this)

  var currentPlayer: Player = controller.player1
  var nextPlayer: Player = controller.player2

  def index = Action {

    Ok(views.html.game(controller.fieldSize,controller.fieldSize))
  }

  def start = Action {
      controller = Controller(10, this)
      currentPlayer = controller.player1
      nextPlayer = controller.player2
      Ok(views.html.setShips(controller,currentPlayer))
  }

  def setShips(x: Int,y:Int, orientationString: String) = Action {
    var orientation: Orientation = null
    if(orientationString == "v") {
      orientation = Orientation.VERTICAL
    } else {
      orientation = Orientation.HORIZONTAL
    }
    println("set ship at x:" + x + "y:" + y)

    val test = controller.placeShip(currentPlayer,Point(x,y),2,orientation)
    println(test)
    Ok(views.html.setShips(controller,currentPlayer))
  }

  override def startGame: Unit = {
    println("starting game")
  }

  override def announceWinner(color: String): Unit = {
    println(color + "won")
  }

  override def playerSwitch(player: Player): Unit =  {
    currentPlayer = player
    if(currentPlayer.COLOR == controller.player1.COLOR) {
      nextPlayer = controller.player2
    } else {
      nextPlayer = controller.player1
    }
  }

  override def printField(field: Field, color: String): Unit = {
    println("printing field")
  }

  override def shootTurn(): Point =  {
    println("now shooting")
    Point(0,0)
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

  override def readOrientation(): Int = {
    println("reading orientation")
    0
  }
}
