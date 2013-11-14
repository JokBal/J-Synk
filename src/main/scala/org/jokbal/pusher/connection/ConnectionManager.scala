package org.jokbal.pusher.connection

import scala.collection.mutable
import org.jokbal.pusher.model.Event
import org.jokbal.pusher.channel.Channel

object ConnectionManager {
  val connections = mutable.HashMap[String, Connection]()

  def connect(connection: Connection) {
    connection.sendTextFrame(Event.established(connection.socketId).toString)
    connections += connection.socketId->connection
    println(connection.socketId + "connect")
  }

  def disconnect(socketId: String) {
    connections -= socketId
  }

}

