package org.jokbal.pusher.verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.eventbus.EventBus
import org.vertx.scala.core.json._
import org.vertx.scala.core.shareddata.SharedData
import org.jokbal.pusher.util.WrappedEventBus
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
  var gcm_apikey:String=null
  var gcm_config:JsonObject=null
  var mongodb_config:JsonObject=null
  var mongodb_address:String=null

  var eventBus:EventBus=null
  var sharedData:SharedData=null

  def init(config:JsonObject,eb:EventBus,sharedData:SharedData)
  {
    port = config.getInteger("port",8000)
    eventBus_prefix = config.getString("eventbus_prefix","Pusher::")
    eventBus = eb

    this.sharedData=sharedData
    sharedData_prefix = config.getString("sharedData_prefix",Pusher.eventBus_prefix)
    external_address =config.getString("commandChannel","pusher_command")
    authorizationChannel = config.getString("authorizationChannel","pusher_auth")

    //permanent channel setting
    permanent_enabled = config.getBoolean("permanent_enable",true)

    //gcm config
    gcm_config = config.getObject("gcm_config",Json.emptyObj())

    gcm_address = gcm_config.getString("address")
    gcm_apikey = config.getString("gcm_apikey")

    //mongo config
    mongodb_config = new JsonObject();
    mongodb_config.putString("address", "mongo.modpusher");
    mongodb_config.putString("db_name", System.getProperty("vertx.mongo.database", "modpusher_mongo_db"))
    mongodb_config.putString("host", System.getProperty("vertx.mongo.host", "localhost"))
    mongodb_config.putNumber("port", Integer.valueOf(System.getProperty("vertx.mongo.port", "27017")))
    mongodb_config.putBoolean("fake", false)

    mongodb_address = mongodb_config.getString("address")

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
    container.deployVerticle("scala:org.jokbal.pusher.verticle.SocketServer",config, 5)
    container.deployVerticle("scala:org.jokbal.pusher.verticle.HttpServerVerticle",config)
    container.deployModule("io.vertx~mod-redis~1.1.3",Pusher.redis_config)
    container.deployModule("io.vertx~mod-mongo-persistor~2.0.0-final",Pusher.mongodb_config)
    container.deployModule("ashertarno~vertx-gcm~2.0.0",Pusher.gcm_config)


  }
}