package org.jokbal.puhser.verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.eventbus.EventBus
import org.vertx.scala.core.json._

object Pusher{
  var apikey = ""
  var secret = ""
  var port:Int =0
  var eventBus_prefix:String=null
  var ExternalCommandChannel:String=null
  var authorizationChannel:String = null
  var sharedData_prefix:String=null
  var redis_enable:Boolean=false
  var redis_address:String=null

  def setConfig(config:JsonObject)
  {
    Pusher.port = config.getInteger("port",8080)
    Pusher.eventBus_prefix = config.getString("eventbus_prefix","Pusher::")
    Pusher.sharedData_prefix = config.getString("sharedData_prefix",Pusher.eventBus_prefix)
    Pusher.ExternalCommandChannel =config.getString("commandChannel","pusher_command")
    Pusher.authorizationChannel = config.getString("authorizationChannel","pusher_auth")
    Pusher.redis_enable =config.getBoolean("redis_enable",false)
    val redis_config = config.getObject("redis_config")
    Pusher.redis_address = redis_config.getString("address")
  }

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
    println("test2")
  }
}