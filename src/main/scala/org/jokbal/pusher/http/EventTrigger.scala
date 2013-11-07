package org.jokbal.pusher.http

import org.vertx.java.core.json.JsonObject
import org.jokbal.pusher.channel.Channel

class EventTrigger(body : String){

  val trigger = new JsonObject(body)
  val eventName = trigger.getString("name")
  val channels = trigger.getArray("channels").toArray.toSeq
  val dataString = trigger.getString("data")
  var statusCode : Int = 200
  var statusMessage : String = null

  def publishEvent() {
    if(checkDataSize){
      for(ch <- channels){
        val channel = ch.toString
        var json = new JsonObject
        json.putString("channel",channel)
        json.putString("event",eventName)
        json.putObject("data",new JsonObject(dataString))

        Channel.publishEvent(channel,json.toString)

      }
    }else{
      statusCode = 413
      statusMessage = "Too large data parameter"
    }

  }

  def checkDataSize() = {
    if(dataString.length >= 10000) false
    else true
  }
}