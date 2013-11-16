package org.jokbal.pusher.connection

import scala.collection.mutable
import org.jokbal.pusher.model.Event
import org.jokbal.pusher.channel.Channel

object ConnectionManager {
  val connections = mutable.HashMap[String, Connection]()

  def connect(connection: Connection) {
    println(connection.socketId + " connected")
    connection.sendTextFrame(Event.established(connection.socketId).toString)
    connections += connection.socketId->connection


  }

  def disconnect(socketId: String) {
    println(socketId + " disconnected")
    connections -= socketId

  }

}

