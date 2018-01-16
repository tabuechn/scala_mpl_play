package controllers

import javax.inject.Inject
import javax.inject.Singleton

import akka.actor.{ActorRef, ActorSystem, Props}
import de.htwg.se.battleship.controller.Controller
import de.htwg.se.battleship.model.Message.StartGame
import de.htwg.se.battleship.view.{GuiView, TuiView}
import de.htwg.se.battleship.model.{Message, Orientation, Player, Point}
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.mvc.WebSocket
import services.WebSocketActorFactory
import de.htwg.se.battleship.model._



@Singleton
class BattleshipController @Inject()(webSocketActorFactory: WebSocketActorFactory, cc: ControllerComponents) extends AbstractController(cc)  {

  val (fieldSize, actorSystemName, controllerActorName) = (10,"battleship","controller")
  val actorSystem = ActorSystem.create(actorSystemName)
  //1x5Felder, 2x4Felder, 3x3Felder, 4x2Felder //Size -> Amount
  val shipInventory: scala.collection.mutable.Map[Int, Int] = scala.collection.mutable.Map(/*5 -> 1, 4 -> 2, 3 -> 3*/ 2 -> 1)
  var controller = actorSystem.actorOf(Controller.props(fieldSize, shipInventory), controllerActorName)
  var tui = actorSystem.actorOf(Props(new TuiView(controller)))
  var gui = actorSystem.actorOf(Props(new GuiView(controller)))

  def start = Action {

    //controller ! Message.StartGame
    Ok(views.html.game())
  }

  def restart = Action {
    actorSystem.stop(controller)
    actorSystem.stop(tui)
    actorSystem.stop(gui)
    controller = actorSystem.actorOf(Controller.props(fieldSize,shipInventory))
    tui = actorSystem.actorOf(Props(new TuiView(controller)))
    gui = actorSystem.actorOf(Props(new GuiView(controller)))
    Ok(views.html.game())
  }

  def setShips(x: Int,y:Int, shipSize:Int, orientationString: String, playerColor: String) = Action {
    var orientation: Orientation = null
    if(orientationString == "v") {
      orientation = VERTICAL
    } else {
      orientation = HORIZONTAL
    }
    println("set ship at x:" + x + "y:" + y)

    controller ! Message.PlaceShipViaColor(playerColor,Point(x,y),shipSize,orientation)
    /*
    val test = controller.placeShip(currentPlayer,Point(x,y),2,orientation)
    println(test) */
    //Ok(views.html.setShips(controller,currentPlayer))
    Ok
  }

  def shootShip(x: Int,y: Int, playerColor: String) = Action {

    controller ! Message.HitShipViaColor(playerColor, Point(x,y))

    Ok
  }

  def socket: WebSocket =  {
    controller ! StartGame
    webSocketActorFactory.create(controller)
  }

}
