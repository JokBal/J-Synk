package org.jokbal.pusher.model

import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.core.json.{Json, JsonObject}

class Data(buffer: Buffer) {
  val jsonObject: JsonObject = new JsonObject(buffer.toString())
  val dataJsonObject: JsonObject = jsonObject.getObject("data",Json.emptyObj())

  var channelDataObject : JsonObject = null

  try{
    channelDataObject = new JsonObject(dataJsonObject.getString("channel_data"))
  }catch{
    case e : Exception => channelDataObject = Json.emptyObj()
  }

  val auth: String = dataJsonObject.getString("auth")
  var channel: String = dataJsonObject.getString("channel")
  val event: String = jsonObject.getString("event")

  if(channel == null) {
    channel = jsonObject.getString("channel")
  }
}