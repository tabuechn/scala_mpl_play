package controllers

import javax.inject.{Inject, Singleton}

import de.htwg.se.battleship.controller.Controller
import de.htwg.se.battleship.view.TuiView
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class BattleshipController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def index = Action {
    val con = new Controller(10, new TuiView)
    Ok(views.html.game(10,10))
  }
}
