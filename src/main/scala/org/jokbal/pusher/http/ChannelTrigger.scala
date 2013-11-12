package org.jokbal.pusher.http

import org.vertx.scala.core.http.HttpServerRequest
import org.jokbal.pusher.sharedstore.ChannelData
import org.jokbal.pusher.sharedstore.SharedStore
import org.vertx.scala.core.json.{JsonObject, JsonArray}

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
    val info = req.params().get("info")
    var attrs : Array[String]= null


    if(info != null){
      attrs = getAttributes(info)
      for(attr <- attrs if(attr.equals("user_count") && !filter.equals("presence-")) ){

        responseCode = 400
        responseMessage = "user_count is attribute only for presence channel"
        return false
      }
    }

    var json = new JsonObject
    var channels = new JsonObject

    for(channel <- channelList){
      val ch = channel.toString
      val info = getInfo(attrs,ch)
      channels.putObject(ch,info)
    }

    json.putObject("channels",channels)
    responseMessage = json.toString
    println(responseCode + " : " + responseCode)
    return true

  }

  def getAttributes(info : String) = {
    info.split(",")
  }

  def getFilteredList(filter : String) = {
    var channelList : Seq[Any] = null
    filter match {
      case "private-" => SharedStore.channelData.privateChannels({
        json : JsonArray => channelList = json.toArray.toSeq
      })
      case "presence-" => SharedStore.channelData.presenceChannels({
        json : JsonArray => channelList = json.toArray.toSeq
      })
      case "" => SharedStore.channelData.Channels({
        json : JsonArray => channelList = json.toArray.toSeq
      })
      case s : String =>
        SharedStore.channelData.Channels({
          json : JsonArray =>
            val list = json.toArray.toSeq
            for(channel <- list if(channel.toString.substring(0,s.length).equals(s))){
              channelList += channel.toString
            }
        })

    }
    channelList
  }

  def getInfo(attrs : Array[String], chName : String) : JsonObject = {
    var info = new JsonObject

    if(attrs == null){
      return info
    }

    for(attr <- attrs){
      attr match {
        case "user_count" =>
          SharedStore.presenceData(chName).getPresenceCount({
          count : Int => info.putNumber("user_count",count)
        })
        case _ =>  // TODO: extra attribute implementation
      }
    }
    return info
  }


}

trait GetChannel extends GetChannels{

  override def get(req : HttpServerRequest) : Boolean= {

    val channelName = req.params().get("channelName")
    val info = req.params().get("info")
    var attrs : Array[String]= null

    if(info != null){
      attrs = getAttributes(info)
      for(attr <- attrs if(attr.equals("user_count") && !channelName.substring(0,9).equals("presence-"))){
        responseCode = 400
        responseMessage = "user_count is attribute only for presence channel"
        return false
      }
    }

    responseMessage = getInfo(attrs,channelName).toString
    println(responseCode + " : " + responseMessage)
    return true

  }

  override def getInfo(attrs : Array[String], chName : String) : JsonObject = {

    var info = super.getInfo(attrs,chName)

    if(info.getNumber("user_count") == 0){
      info.putBoolean("occupied",false)
    }else{
      info.putBoolean("occupied",true)
    }

    return info
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
      responseMessage = json.toString
      println(responseCode + " : " + responseCode)
      return true
    }


  }
}