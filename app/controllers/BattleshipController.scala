package controllers

import javax.inject.{Inject, Singleton}

import de.htwg.se.battleship.controller.Controller
import de.htwg.se.battleship.view.TuiView
import play.api.mvc.{AbstractController, ControllerComponents}

import play.api.mvc._
import akka.stream.scaladsl._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class BattleshipController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def index = Action {
    val con = new Controller(10, new TuiView)
    Ok(views.html.game(10,10))
  }

  def socket = WebSocket.accept[String, String] { request =>

    // Log events to the console
    val in = Sink.foreach[String](println)

    // Send a single 'Hello!' message and then leave the socket open
    val out = Source.single("Hello!").concat(Source.maybe)

    Flow.fromSinkAndSource(in, out)
  }

}
