package org.jokbal.pusher.verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.http.ServerWebSocket
import org.jokbal.pusher.connection.{WebSocketConnection, ConnectionManager}
import org.vertx.scala.core.eventbus.Message

class SocketServer extends Verticle {

  override def start() {

    val conf = container.config()
    Pusher.init(conf,vertx.eventBus,vertx.sharedData)

    vertx.createHttpServer()
      .setAcceptBacklog(2000000)
      .setSendBufferSize(1024*4)
      .setReceiveBufferSize(1024*4)
      .websocketHandler(
    {
      socket:ServerWebSocket => {
        webSocketOpenHandler(socket)
      }
    }) listen(Pusher.port)
    println("WebSocket Server Listening on " + Pusher.port)
  }

  def webSocketOpenHandler(socket: ServerWebSocket):Unit = {
    ConnectionManager.connect(new WebSocketConnection(socket))
  }
}