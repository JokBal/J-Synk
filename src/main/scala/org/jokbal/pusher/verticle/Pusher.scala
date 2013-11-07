package org.jokbal.pusher.verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.eventbus.EventBus

object Pusher{
  var apikey = ""
  var secret = ""
  var eventbus_prefix = ""
  var shareddata_prefix = ""

}

class Pusher extends Verticle {
  var eventBus: EventBus = null

  override def start() {
    initialization
  }

  def initialization() {
    eventBus = vertx.eventBus
    container.deployVerticle("scala:org.jokbal.pusher.verticle.SocketServer")
  }
}