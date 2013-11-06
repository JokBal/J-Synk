package net

import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.core.http.ServerWebSocket
import org.vertx.scala.core.sockjs.SockJSSocket
import org.vertx.scala.core.FunctionConverters._
import java.util.UUID

abstract class Connection {
  val socketId = UUID.randomUUID().toString
  def sendTextFrame(str: String)
}


class WebSocketConnection(socket: ServerWebSocket) extends Connection {
  socket.dataHandler(DataHandler.handle(this) _)
  socket.closeHandler(fnToHandler(CloseHandler.handle(socketId) _))
  def sendTextFrame(str: String) {
    socket.writeTextFrame(str)
  }
}


class SockJsConnection(socket: SockJSSocket) extends Connection {
  socket.dataHandler(DataHandler.handle(this) _)
  def sendTextFrame(str: String) {
    socket.write(Buffer.apply(str))
  }
}