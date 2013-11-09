package org.jokbal.pusher.verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.http.{HttpServer, ServerWebSocket}
import org.jokbal.pusher.connection.{WebSocketConnection, SockJsSocketConnection, ConnectionManager}
import org.vertx.scala.core.sockjs.{SockJSSocket, SockJSServer}
import org.vertx.scala.core.json.JsonObject

class SocketServer extends Verticle {

  override def start() {

    val conf = container.config()
    Pusher.init(conf,vertx.eventBus,vertx.sharedData)

    val httpServer: HttpServer = vertx.createHttpServer()
    val sockJsServer: SockJSServer = vertx.createSockJSServer(httpServer)
    httpServer.websocketHandler(webSocketOpenHandler _)
    sockJsServer.installApp(new JsonObject().putString("prefix", "/pusher"), sockJsSocketOpenHandler _)
    httpServer listen Pusher.port
    println("WebSocket Server Listening on " + Pusher.port)
    println("SockJs Server Listening on " + Pusher.port)
  }

  def webSocketOpenHandler(socket: ServerWebSocket):Unit = {
    ConnectionManager.connect(new WebSocketConnection(socket))
  }

  def sockJsSocketOpenHandler(socket: SockJSSocket) {
    ConnectionManager.connect(new SockJsSocketConnection(socket))
  }
}