package org.jokbal.pusher.connection

import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.core.http.ServerWebSocket
import org.vertx.scala.core.sockjs.SockJSSocket
import org.vertx.scala.core.FunctionConverters._
import java.util.UUID
import org.jokbal.pusher.model.{Event, Data}
import org.jokbal.pusher.channel.Channel

abstract class Connection {
  val socketId = UUID.randomUUID().toString

  def sendTextFrame(str: String)

  def dataHandler(connection: Connection)(buffer: Buffer) {
    val data = new Data(buffer)
    data.event match {
      case Event.SUBSCRIBE => Channel(data.channel).subscribe(connection, data.dataJsonObject)
      case Event.UNSUBSCRIBE => {Channel(data.channel).unsubscribe(connection)}
      case Event.PING =>  {
        connection.sendTextFrame(Event.pong.toString)
      }
      case Event.PONG => {
        connection.sendTextFrame(Event.ping.toString)
      }
      case Event.CLIENT_EVENT(c) => {
        val channel = Channel(data.channel)
        //if(channel.isClientTriggerEnabled) {
          println("Client Event is triggered" + data.channel)
          channel.publishEvent(data.event, data.dataJsonObject)
        //}
      }
      case _ => {

      }
    }
  }
  def closeHandler(connection: Connection)(void:Void) {
    Channel.disconnectConnection(connection)
    ConnectionManager.disconnect(connection.socketId)
  }
}

class WebSocketConnection(socket: ServerWebSocket) extends Connection {

  socket.dataHandler(dataHandler(this) _)
  socket.closeHandler(closeHandler(this) _)

  def sendTextFrame(str: String) {
    socket.writeTextFrame(str)
  }
}

class SockJsSocketConnection(socket: SockJSSocket) extends Connection {
  socket.dataHandler(dataHandler(this) _)
  def sendTextFrame(str: String) {
    socket.write(Buffer.apply(str))
  }
}