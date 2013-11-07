package org.jokbal.puhser.verticle

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
    val config = container.config()
    initialization
  }

  def initialization() {
    eventBus = vertx.eventBus
    println("test")
    container.deployVerticle("scala:org.jokbal.puhser.verticle.SocketServer")
    container.deployVerticle("scala:org.jokbal.puhser.http.HttpVerticle")
    println("test2")
  }
}