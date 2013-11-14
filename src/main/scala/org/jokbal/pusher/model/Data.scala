package org.jokbal.pusher.model

import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.core.json.{Json, JsonObject}

class Data(buffer: Buffer) {
  val jsonObject: JsonObject = new JsonObject(buffer.toString())
  val dataJsonObject: JsonObject = jsonObject.getObject("data",Json.emptyObj())
  val channelDataObject : JsonObject = Json.fromObjectString(dataJsonObject.getString("channel_data"))
  var channel: String = dataJsonObject.getString("channel")
  val auth: String = dataJsonObject.getString("auth")
  val event: String = jsonObject.getString("event")

  if(channel == null) {
    channel = jsonObject.getString("channel")
  }
}