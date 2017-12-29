package controllers

import javax.inject.{Inject, Singleton}

import de.htwg.se.battleship.controller.Controller
import de.htwg.se.battleship.view.TuiView
import play.api.mvc.{AbstractController, ControllerComponents}

import play.api.mvc._
import akka.stream.scaladsl._

@Singleton
class BattleshipController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  val controller: Controller = Controller(10, new TuiView())

  def index = Action {

    Ok(views.html.game(controller.fieldSize,controller.fieldSize))
  }

}
