package org.jokbal.pusher.verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.eventbus.EventBus
import org.vertx.scala.core.json._
import org.vertx.scala.core.shareddata.SharedData
import org.jokbal.pusher.sharedstore.SharedStore
import org.vertx.scala.core.eventbus.Message
import org.vertx.java.core.json.JsonObject
import org.vertx.scala.core.json.JsonObject

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
  var gcm_config:JsonObject=null
  var mongodb_config:JsonObject=null
  var mongodb_address:String=null

  var eventBus:EventBus=null
  var sharedData:SharedData=null

  def init(config:JsonObject,eb:EventBus,sharedData:SharedData)
  {
    apikey = config.getString("apikey"."somestring")
    secret = config.getString("secret"."somesecretstring")
    port = config.getInteger("port",8000)
    eventBus_prefix = config.getString("eventbus_prefix","J-Synk::")
    eventBus = eb

    this.sharedData=sharedData
    sharedData_prefix = config.getString("sharedData_prefix",Pusher.eventBus_prefix)
    external_address =config.getString("address","pusher_command")
    //authorizationChannel = config.getString("authorizationChannel","pusher_auth")

    //permanent channel setting
    permanent_enabled = config.getBoolean("permanent_enable",true)
    gcm_config = config.getObject("gcm_config",Json.emptyObj())
    gcm_address = gcm_config.getString("address","")

    if(gcm_address.equals(""))
    {
      gcm_address="gcm_address"
      gcm_config.putString("address",gcm_address)
    }

    //mongo config
    mongodb_config = config.getObejct("mongodb_config",Json.emptyObj())
    mongodb_config.putString("address", "mongo.modpusher")

    mongodb_address = mongodb_config.getString("address","")

    if(mongodb_address.equals(""))
    {
      mongodb_address="J-Synk::mongodb"
      mongodb_config.putString("address",mongodb_address)
    }



    //redis config

    redis_enabled =config.getBoolean("redis_enable",true)
    if(redis_enabled) SharedStore.enableRedis()

    redis_config = new JsonObject()

    redis_config.putString("address", "redis.modpusher")
    redis_config.putString("host", "localhost")
    redis_config.putNumber("port", 6379)

    redis_address = redis_config.getString("address")



  }
}

class Pusher extends Verticle {

  override def start() {
    val config = container.config()
    Pusher.init(config,vertx.eventBus,vertx.sharedData)
    val socketInstance = config.getInteger("socket_server_instance",1)
    val apiInstance = config.getInteger("api_server_instance",1)
    val redisInstance = config.getInteger("redis_instance",1)
    val mongoInstance = config.getInteger("mongo_instance",1)

    container.deployVerticle("scala:org.jokbal.pusher.verticle.SocketServer",config, socketInstance)
    container.deployVerticle("scala:org.jokbal.pusher.verticle.HttpServerVerticle",config,apiInstance)
    if(Pusher.permanent_enabled)
      container.deployVerticle("scala:org.jokbal.pusher.verticle.GCMSenderVerticle",Pusher.gcm_config)
    if(Pusher.redis_enabled)
      container.deployModule("io.vertx~mod-redis~1.1.3",redisInstance)
    if(Pusher.permanent_enabled)
      container.deployModule("io.vertx~mod-mongo-persistor~2.0.0-final",mongoInstance)



  }
}