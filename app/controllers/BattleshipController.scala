package controllers

import javax.inject.Inject
import javax.inject.Singleton

import akka.actor.{ActorRef, ActorSystem, Props}
import de.htwg.se.battleship.controller.Controller
import de.htwg.se.battleship.model.{Message, Orientation, Player, Point}
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.mvc.WebSocket
import services.WebSocketActorFactory


@Singleton
class BattleshipController @Inject()(webSocketActorFactory: WebSocketActorFactory, cc: ControllerComponents) extends AbstractController(cc)  {

  val (fieldSize, actorSystemName, controllerActorName) = (10,"battleship","controller")
  val actorSystem = ActorSystem.create(actorSystemName)
  val controller = actorSystem.actorOf(Controller.props(fieldSize), controllerActorName)
  val wui = actorSystem.actorOf(Props(new WUI(controller)))

  def start = Action {

      //Ok(views.html.setShips(controller,currentPlayer))
    controller ! Message.StartGame

    Ok(views.html.game(10,10))
  }

  def setShips(x: Int,y:Int, orientationString: String) = Action {
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
    Ok(views.html.game(10,10))
  }

  def socket: WebSocket = webSocketActorFactory.create(controller)

}
