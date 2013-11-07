package org.jokbal.pusher.Model

import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.core.json.JsonObject

class DataModel(buffer: Buffer) {
  private val jsonObject: JsonObject = new JsonObject(buffer.toString())
  private val dataJsonObject: JsonObject = jsonObject.getObject("data")
  val channelData : JsonObject = dataJsonObject.getObject("channel_data")
  val channel: String = dataJsonObject.getString("channel")
  val auth: String = dataJsonObject.getString("auth")
  val event: String = jsonObject.getString("event")
}