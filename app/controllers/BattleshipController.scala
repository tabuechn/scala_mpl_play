package controllers

import javax.inject.{Inject, Singleton}

import de.htwg.se.battleship.controller.Controller
import de.htwg.se.battleship.view.TuiView
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.mvc._
import akka.stream.scaladsl._
import de.htwg.se.battleship.model.{Orientation, Player}

@Singleton
class BattleshipController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  val controller: Controller = Controller(10, new TuiView())

  var currentPlayer: Player = null

  def index = Action {

    Ok(views.html.game(controller.fieldSize,controller.fieldSize))
  }

  def start = Action {
    currentPlayer = controller.player1.copy()
     Ok(views.html.setShips(controller,currentPlayer))
  }

  def setShips = Action {
    Ok(views.html.setShips(controller,currentPlayer))
  }

}
