package org.jokbal.pusher.http

import org.vertx.scala.core.http.HttpServerRequest
import org.jokbal.pusher.sharedstore.SharedStore
import org.vertx.scala.core.json.{Json, JsonObject, JsonArray}


class ChannelTrigger{

}

trait GetChannels{

  var responseCode : Int = 200
  var responseMessage : String = null

  def getCode = {
    responseCode
  }
  def getMessage = {
    responseMessage
  }

  def get(req : HttpServerRequest) : Boolean= {

    var filter = req.params().get("filter_by_prefix")

    if(filter == null){
      filter = ""
    }

    val channelList = getFilteredList(filter)

    var json = new JsonObject
    var channels = new JsonObject

    for(channel <- channelList){
      val ch = channel.toString
      println(ch)

      var info : JsonObject = Json.emptyObj()

      if(ch.contains("presence-")) info = getInfo(ch)

      channels.putObject(ch,info)
    }

    json.putObject("channels",channels)
    println(json.toString)
    responseMessage = json.toString
    println(responseCode + " : " + responseCode)
    return true

  }

  def getFilteredList(filter : String) : Array[Object] = {

    var channelList : Array[Object] = null

    filter match {
      case "private-" => SharedStore.channelData.privateChannels({
        json : JsonArray =>
          println("case 1" + json.toString)
          channelList = json.toArray


      })
      case "presence-" => SharedStore.channelData.presenceChannels({
        json : JsonArray =>
          println("case 2" + json.toString)
          channelList = json.toArray

      })
      case "" => SharedStore.channelData.Channels({
        json : JsonArray =>
          println("case 3" + json.toString)
          channelList = json.toArray
      })

    }
    return channelList
  }

  def getInfo(chName : String) : JsonObject = {
    var info = new JsonObject

    SharedStore.presenceData(chName).getPresenceCount({
      count : Int => info.putNumber("user_count",count)
    })

    return info
  }

}

trait GetChannel extends GetChannels{

  override def get(req : HttpServerRequest) : Boolean= {

    val channelName = req.params().get("channelName")

    responseMessage = getInfo(channelName).toString
    println(responseCode + " : " + responseMessage)
    return true

  }

  override def getInfo(chName : String) : JsonObject = {

    var info : JsonObject = null

    if(chName.contains("presence-")){
      info = super.getInfo(chName)

      if(info.getNumber("user_count") == 0){
        info.putBoolean("occupied",false)
      }else{
        info.putBoolean("occupied",true)
      }
      return info

    }else{
      info = Json.emptyObj()
      return info
    }


  }

}

trait GetUsers{

  var responseCode : Int = 200
  var responseMessage : String = null

  def getCode = {
    responseCode
  }

  def getMessage = {
    responseMessage
  }

  def get(req : HttpServerRequest) : Boolean = {

    val channelName = req.params().get("channelName")
    var json = new JsonObject
    var users = new JsonArray
    if(!channelName.substring(0,9).equals("presence-")){
      responseCode = 400
      responseMessage = "/users API is only for presence channel"
      return false
    }else{
      SharedStore.presenceData(channelName).getPresence({
        json : JsonObject =>
          val ids = json.getFieldNames.toArray()
          for(id <-ids){
            var temp = new JsonObject
            temp.putString("id",id.toString)
            users.addObject(temp)
          }
      })
      json.putArray("users",users)
      println(json.toString)
      responseMessage = json.toString
      println(responseCode + " : " + responseCode)
      return true
    }


  }
}