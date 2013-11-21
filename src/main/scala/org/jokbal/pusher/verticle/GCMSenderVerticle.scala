package org.jokbal.pusher.verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.http.HttpClientResponse
import org.vertx.scala.core.json._
import org.vertx.scala.core.eventbus.Message
import org.vertx.scala.core.buffer.Buffer

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 17.
 * Time: 오후 8:14
 * To change this template use File | Settings | File Templates.
 */
class GCMSenderVerticle extends Verticle{
  override def start(){
    val config = container.config()
    val address = config.getString("address")
    val api_key = config.getString("api_key")

    vertx.eventBus.registerHandler(address,{
      msg:Message[JsonObject]=>
        sendGCM(msg.body())
    })

    def sendGCM(data:JsonObject){
      val req = vertx.createHttpClient()
        .setHost("android.googleapis.com")
        .setKeepAlive(true)
        .post("/gcm/send",{response:HttpClientResponse=>response.bodyHandler({buffer:Buffer=>println("resp :: "+buffer.toString())})})
        .putHeader("Authorization","key="+api_key)
        .putHeader("Content-Type","application/json")
        .end(data.toString,"UTF-8")
    }
  }


}
