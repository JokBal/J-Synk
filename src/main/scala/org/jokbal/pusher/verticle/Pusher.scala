package org.jokbal.pusher.verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.eventbus.EventBus
import org.vertx.scala.core.json._
import org.vertx.scala.core.shareddata.SharedData
import org.jokbal.pusher.util.WrappedEventBus
import org.jokbal.pusher.sharedstore.SharedStore
import org.vertx.scala.core.eventbus.Message

object Pusher{
  var apikey = ""
  var secret = ""
  var port:Int =0
  var eventBus_prefix:String=null
  var external_address:String=null
  var authorizationChannel:String = null
  var sharedData_prefix:String=null
  var redis_enabled:Boolean=false
  var redis_address:String=null
  var redis_config:JsonObject=null
  var permanent_enabled:Boolean=false
  var gcm_address:String=null
  var gcm_apikey:String=null
  var gcm_config:JsonObject=null
  var mongodb_address:String=null
  var mongodb_config:JsonObject=null

  var eventBus:WrappedEventBus=null
  var sharedData:SharedData=null

  def init(config:JsonObject,eb:EventBus,sharedData:SharedData)
  {
    port = config.getInteger("port",8000)
    eventBus_prefix = config.getString("eventbus_prefix","Pusher::")
    eventBus = new  WrappedEventBus(eventBus_prefix,eb)

    this.sharedData=sharedData
    sharedData_prefix = config.getString("sharedData_prefix",Pusher.eventBus_prefix)
    external_address =config.getString("commandChannel","pusher_command")
    authorizationChannel = config.getString("authorizationChannel","pusher_auth")

    redis_enabled =config.getBoolean("redis_enable",false)
    if(redis_enabled) SharedStore.enableRedis()

    redis_config = config.getObject("redis_config",Json.emptyObj())
    redis_address = redis_config.getString("address")

    permanent_enabled = config.getBoolean("permanent_enable",false)
    gcm_config = config.getObject("gcm_config",Json.emptyObj())
    gcm_address = gcm_config.getString("address")
    gcm_apikey = config.getString("gcm_apikey")
  }
}

class Pusher extends Verticle {

  override def start() {
    val config = container.config()
    Pusher.init(config,vertx.eventBus,vertx.sharedData)
    container.deployVerticle("scala:org.jokbal.pusher.verticle.SocketServer",config, 5)
    container.deployVerticle("scala:org.jokbal.pusher.verticle.HttpServerVerticle",config)
    container.deployModule("io.vertx~mod-redis~1.1.3",Pusher.redis_config)
    container.deployModule("io.vertx~mod-mongo-persistor~2.0.0-final",Pusher.mongodb_config)
    container.deployModule("ashertarno~vertx-gcm~2.0.0",Pusher.gcm_config)


  }
}