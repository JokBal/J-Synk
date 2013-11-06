package verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.eventbus.EventBus

class Main extends Verticle {

  var eventBus: EventBus = null

  override def start() {
    initialization
  }

  def initialization() {
    eventBus = vertx.eventBus
    container.deployVerticle("SocketServer")
  }
}