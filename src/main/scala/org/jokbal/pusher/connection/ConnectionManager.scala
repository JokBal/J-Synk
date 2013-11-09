package org.jokbal.pusher.connection

import scala.collection.mutable
import org.vertx.scala.core.json.JsonObject
import org.jokbal.pusher.model.Event

object ConnectionManager {
  val connections = mutable.HashMap[String, Connection]()

  def connect(connection: Connection) {
    println("connect");
    connection.sendTextFrame(Event.established(connection.socketId).toString)
    connections += connection.socketId->connection
  }

  def disconnect(socketId: String) {
    println("disconnect");
    connections -= socketId
  }

}

