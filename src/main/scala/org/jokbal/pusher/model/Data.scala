package org.jokbal.pusher.model

import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.core.json.JsonObject

class Data(buffer: Buffer) {
  val jsonObject: JsonObject = new JsonObject(buffer.toString())
  val dataJsonObject: JsonObject = jsonObject.getObject("data")
  val channelDataObject : JsonObject = dataJsonObject.getObject("channel_data")
  var channel: String = dataJsonObject.getString("channel")
  val auth: String = dataJsonObject.getString("auth")
  val event: String = jsonObject.getString("event")

  if(channel == null) {
    channel = jsonObject.getString("channel");
  }
}