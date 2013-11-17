package org.jokbal.pusher.sharedstore

import org.jokbal.pusher.verticle.Pusher
import org.vertx.scala.core.json._
import org.vertx.scala.core.eventbus.Message

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 11.
 * Time: 오후 6:50
 * To change this template use File | Settings | File Templates.
 */
class PermanentData {
  val PERMANENT_TABLE = Pusher.sharedData_prefix+"premanentData"

  def insertMobile(channel:String,mobileKey:String){
    val data = Json.obj("channel"->channel,"mobile"->mobileKey)
    val request = Json.obj("action"->"save","collection"->PERMANENT_TABLE,"document"->data)
    println(request.toString)
    Pusher.eventBus.send(Pusher.mongodb_address,request,handleResult _)
  }

  def deleteMobile(channel:String,mobileKey:String){
    val data = Json.obj("channel"->channel,"mobile"->mobileKey)
    val request = Json.obj("action"->"delete","collection"->PERMANENT_TABLE,"matcher"->data)
    Pusher.eventBus.send(Pusher.mongodb_address,request,handleResult _)
  }

  def getMobile(channel:String,callback:JsonArray=>Unit){
    val data = Json.obj("channel"->channel)
    val request = Json.obj("action"->"find","collection"->PERMANENT_TABLE,"matcher"->data)
    println("getMobileCalled"+request.toString)
    def resultCallback(msg:Message[JsonObject]){
      println("mobile result callbakc called"+msg.body.toString)
      val status = msg.body.getString("status")
      if("error".equals(status))
      {error(msg.body.getString("message"));return}
      callback.apply(msg.body.getArray("results"))
    }
    Pusher.eventBus.send(Pusher.mongodb_address,request,resultCallback _)
  }

  def handleResult(msg:Message[JsonObject]){
    val status = msg.body.getString("status")
    if("error".equals(status))
      error(msg.body.getString("message"));return
  }

  def error(message:String){
    println(message)
  }
}
