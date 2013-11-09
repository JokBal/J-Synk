package org.jokbal.pusher.connection

import scala.collection.mutable
import org.vertx.scala.core.json.JsonObject
import org.vertx.scala.core.buffer.Buffer
import org.jokbal.pusher.model.{Event, Data}
import org.jokbal.pusher.channel.Channel

object ConnectionManager {
  val SUBSCRIBE = "pusher:subscribe"
  val UNSUBSCRIBE = "pusher:unsubscribe"
  val PING = "pusher:ping"
  val PONG = "pusher:pong"
  val PUSHER_CONNECTION_ESTABLISHED = "pusher:connection_established"

  val connections = mutable.HashMap[String, Connection]()

  def connect(connection: Connection) {
    val jsonObject = new JsonObject()
    jsonObject.putString("socket_id", connection.socketId)
    connection.sendTextFrame(Event.apply(PUSHER_CONNECTION_ESTABLISHED, jsonObject).toString)
    connections += connection.socketId->connection
  }

  def dataHandler(connection: Connection)(buffer: Buffer) {
    val data = new Data(buffer)
    data.event match {
      case SUBSCRIBE => Channel.apply(data.channel).subscribe(connection, data.dataJsonObject)
      case UNSUBSCRIBE => Channel.apply(data.channel).unsubscribe(connection)
      case PING =>  {
        connection.sendTextFrame(Event.apply(PONG, new JsonObject).toString)
      }
      case PONG => {
        connection.sendTextFrame(Event.apply(PING, new JsonObject).toString)
      }
    }
  }

  def closeHandler(socketId: String)(void:Void) {
    connections -= socketId
  }
}

