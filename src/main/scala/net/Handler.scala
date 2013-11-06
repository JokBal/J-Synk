package net

import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.core.json.JsonObject

object DataHandler{

  val SUBSCRIBE = "pusher:subscribe"
  val UNSUBSCRIBE = "pusher:unsubscribe"
  val PING = "pusher:ping"
  val PONG = "pusher:pong"
  val PUSHER_INTERNAL_SUBSCRIPTION_SUCCEEDED = "pusher_internal:subscription_succeeded"
  val PUSHER_INTERNAL_MEMBER_ADDED = "pusher_internal:member_added"
  val PUSHER_INTERNAL_MEMBER_REMOVED = "pusher_internal:member_removed"

  def handle(connection: Connection)(buffer: Buffer) {
    val data = new Data(buffer)
    val response: JsonObject = data.event match {
      case PING => ping()
      case PONG => pong()
      case SUBSCRIBE => subscribe(data)
      case UNSUBSCRIBE => unsubscribe(data)
    }
  }

  def ping(): JsonObject = {
    getResponseObject("pusher:pong", new JsonObject())
  }

  def pong(): JsonObject = {
    getResponseObject("pusher:ping", new JsonObject())
  }

  def subscribe(data: Data): JsonObject = {
    null
  }

  def unsubscribe(data: Data): JsonObject = {
    null
  }

  def getResponseObject(eventName: String, dataJsonObject: JsonObject, optionalName: String=null, optionalData: String=null): JsonObject =
  {
    val response = new JsonObject
    response.putString("event", eventName)
    response.putObject("data", dataJsonObject)
    if(optionalName != null) response.putString(optionalName, optionalData)
    response
  }
}

object CloseHandler {
  def handle(socketId: String)(void:Void) {
    ConnectionManager.disconnect(socketId)
  }
}