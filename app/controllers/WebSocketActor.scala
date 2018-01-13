package controllers

import akka.actor.{Actor, ActorRef, Props}

object WebSocketActor {
  def props(webSocketOut: ActorRef, controllerActor: ActorRef) =
    Props(new WebSocketActor(webSocketOut, controllerActor))
}

class WebSocketActor(socketOut: ActorRef, controller: ActorRef) extends Actor{
  override def receive: Receive = {
    case _ => println("got message")
  }
}
