package controllers

import javax.inject._

import de.htwg.se.battleship.Battleship
import de.htwg.se.battleship.controller.Controller
import de.htwg.se.battleship.view.TuiView
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {

    /*val controller = Controller(10, new TuiView())
    controller.gameStart()*/
    Ok(views.html.index("Your new application is ready. test"))
  }

}
