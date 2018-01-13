package services

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import com.google.inject.Inject
import javax.inject.Singleton

import controllers.WebSocketActor

import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket

@Singleton
class WebSocketActorFactory @Inject()(implicit actorSystem: ActorSystem,
                                      materializer: Materializer) {

  def create(controller: ActorRef): WebSocket =
    WebSocket.accept[JsValue, JsValue] { _ =>
      ActorFlow.actorRef(out => WebSocketActor.props(out,controller))
    }

}