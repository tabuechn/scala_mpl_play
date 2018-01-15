package controllers

import javax.inject.Inject
import javax.inject.Singleton

import akka.actor.{ActorRef, ActorSystem, Props}
import de.htwg.se.battleship.controller.Controller
import de.htwg.se.battleship.model.Message.StartGame
import de.htwg.se.battleship.view.TuiView
import de.htwg.se.battleship.model.{Message, Orientation, Player, Point}
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.mvc.WebSocket
import services.WebSocketActorFactory


@Singleton
class BattleshipController @Inject()(webSocketActorFactory: WebSocketActorFactory, cc: ControllerComponents) extends AbstractController(cc)  {

  val (fieldSize, actorSystemName, controllerActorName) = (10,"battleship","controller")
  val actorSystem = ActorSystem.create(actorSystemName)
  val controller = actorSystem.actorOf(Controller.props(fieldSize), controllerActorName)
  //controller ! StartGame
  val tui = actorSystem.actorOf(Props(new TuiView(controller)))

  def start = Action {

    //controller ! Message.StartGame

    Ok(views.html.game())
  }

  def setShips(x: Int,y:Int, shipSize:Int, orientationString: String) = Action {
    var orientation: Orientation = null
    if(orientationString == "v") {
      orientation = Orientation.VERTICAL
    } else {
      orientation = Orientation.HORIZONTAL
    }
    println("set ship at x:" + x + "y:" + y)

    /*
    val test = controller.placeShip(currentPlayer,Point(x,y),2,orientation)
    println(test) */
    //Ok(views.html.setShips(controller,currentPlayer))
    Ok("ok!")
  }

  def socket: WebSocket = webSocketActorFactory.create(controller)

}
