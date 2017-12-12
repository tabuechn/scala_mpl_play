package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class BattleshipController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def index = Action {
    Ok(views.html.game(10,10))
  }
}
