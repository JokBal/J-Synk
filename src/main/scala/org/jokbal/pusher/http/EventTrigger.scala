package org.jokbal.pusher.http

import org.vertx.java.core.json.JsonObject
import org.jokbal.pusher.channel.Channel

class EventTrigger(body : String){
  val trigger = new JsonObject(body)
  val eventName = trigger.getString("name")
  val channels = trigger.getArray("channels").toArray.toSeq
  val channel = trigger.getString("channel")
  val dataString = trigger.getString("data")
  var responseCode : Int = 200
  var responseMessage : String = "ACCEPTED"

  def publishEvent() : Boolean = {

    if(channel != null){
      if(checkDataSize){
        var json = new JsonObject
        json.putString("channel",channel)
        json.putString("event",eventName)
        json.putObject("data",new JsonObject(dataString))
        try{
          Channel.publishEvent(channel,json.toString)
          println(json.toString)
          return true
        }catch{
          case e:NullPointerException => {
            responseCode = 400
            responseMessage = "There doesn't exist channel " + channel
            return false
          }
        }
      }
    }

    if(checkDataSize){
      for(ch <- channels){
        val channel = ch.toString
        var json = new JsonObject
        json.putString("channel",channel)
        json.putString("event",eventName)
        json.putObject("data",new JsonObject(dataString))

        try{
          Channel.publishEvent(channel,json.toString)
          //println(json.toString)
        }catch{
          case e:NullPointerException => {
            responseCode = 400
            responseMessage = "There doesn't exist channel " + channel
            return false
          }
        }

      }
    }else{
      responseCode = 413
      responseMessage = "Too large data parameter"
      return false
    }

    //println(responseCode + " : " + responseMessage)

    return true

  }

  def checkDataSize() = {
    if(dataString.length >= 10000) false
    else true
  }
}