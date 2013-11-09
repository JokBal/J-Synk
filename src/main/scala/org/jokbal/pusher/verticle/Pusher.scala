package org.jokbal.puhser.verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.eventbus.{MessageData, EventBus}
import org.vertx.scala.core.json._
import org.vertx.scala.core.shareddata.SharedData
import org.vertx.java.core.eventbus.Message
import org.vertx.scala.core.eventbus
import org.jokbal.pusher.util.WrappedEventBus

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

  var eventBus:WrappedEventBus=null
  var sharedData:SharedData=null

  def init(config:JsonObject,eventBus:EventBus,sharedData:SharedData)
  {
    Pusher.port = config.getInteger("port",8080)
    Pusher.eventBus_prefix = config.getString("eventbus_prefix","Pusher::")
    Pusher.eventBus = new  WrappedEventBus(eventBus_prefix,eventBus)
    Pusher.sharedData_prefix = config.getString("sharedData_prefix",Pusher.eventBus_prefix)
    Pusher.ExternalCommandChannel =config.getString("commandChannel","pusher_command")
    Pusher.authorizationChannel = config.getString("authorizationChannel","pusher_auth")
    Pusher.redis_enable =config.getBoolean("redis_enable",false)
    val redis_config = config.getObject("redis_config",Json.emptyObj())
    Pusher.redis_address = redis_config.getString("address")
  }
}

class Pusher extends Verticle {

  override def start() {
    val config = container.config()
    Pusher.init(config,vertx.eventBus,vertx.sharedData)
    container.deployVerticle("scala:org.jokbal.puhser.verticle.SocketServer",config)
    container.deployVerticle("scala:org.jokbal.puhser.verticle.HttpServer",config)

  }
}