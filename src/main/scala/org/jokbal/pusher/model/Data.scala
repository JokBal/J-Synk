package org.jokbal.pusher.model

import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.core.json.JsonObject

class Data(buffer: Buffer) {
  private val jsonObject: JsonObject = new JsonObject(buffer.toString())
  val dataJsonObject: JsonObject = jsonObject.getObject("data")
  val channelDataObject : JsonObject = dataJsonObject.getObject("channel_data")
  val channel: String = dataJsonObject.getString("channel")
  val auth: String = dataJsonObject.getString("auth")
  val event: String = jsonObject.getString("event")
}