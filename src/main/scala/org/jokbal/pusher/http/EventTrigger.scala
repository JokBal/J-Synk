package org.jokbal.pusher.http

import org.vertx.java.core.json.JsonObject
import org.jokbal.pusher.channel.Channel

class EventTrigger(body : String){

  val trigger = new JsonObject(body)
  val eventName = trigger.getString("name")
  val channels = trigger.getArray("channels")
  val dataString = trigger.getString("data")
  var data = null
  var statusCode = null
  var statusMessage = null

  def publishEvent() {
    if(checkData){
      for(channel <- channels){
        var json = new JsonObject
        json.putString("channel",channel)
        json.putString("event",eventName)
        json.putObject("data",data)


        Channel(channel).publishEvent(eventName,json.toString)

        statusCode = 200
        statusMessage = "Successful Event Triggering to " + channel
      }
    }else{

      statusCode = 413
      statusMessage = "Too large data parameter"

    }

  }

  def checkData(){
    if(dataString.length >= 10000) false
    else true
  }




}