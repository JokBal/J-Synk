package net

import org.vertx.scala.core.buffer.Buffer

object DataHandler{
  def handle(connection: Connection)(data:Buffer) {
    connection.sendTextFrame(data.toString)
  }
}

object CloseHandler {
  def handle(socketId: String)(void:Void) {
    ConnectionManager.disconnect(socketId)
  }
}