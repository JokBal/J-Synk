package org.jokbal.pusher.net

import scala.collection.mutable
import org.vertx.scala.core.json.JsonObject

object ConnectionManager {
  val connections = mutable.HashMap[String, Connection]()

  def connect(connection: Connection) {
    val jsonObject = new JsonObject()
    jsonObject.putString("socket_id", connection.socketId)
    connection.sendTextFrame(DataHandler.getResponseObject("pusher:connection_established", jsonObject) toString)
    connections += connection.socketId->connection
  }

  def disconnect(socketId: String) {
    connections -= socketId
  }
}

