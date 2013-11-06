package verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.http.{HttpServer, ServerWebSocket}
import net.{WebSocketConnection, SockJsSocketConnection, ConnectionManager}
import org.vertx.scala.core.sockjs.{SockJSSocket, SockJSServer}
import org.vertx.scala.core.json.JsonObject

class SocketServer extends Verticle {
  override def start() {
    val httpServer: HttpServer = vertx.createHttpServer()
    val sockJsServer: SockJSServer = vertx.createSockJSServer(httpServer)
    httpServer.websocketHandler(webSocketOpenHandler _)
    sockJsServer.installApp(new JsonObject().putString("prefix", "/"), sockJsSocketOpenHandler _)
    httpServer listen 8080
    println("WebSocket server listening on port 8080")
    println("SockJsSocket server listening on port 8080")
  }

  def webSocketOpenHandler(socket: ServerWebSocket):Unit = {
    ConnectionManager.connect(new WebSocketConnection(socket))
  }

  def sockJsSocketOpenHandler(socket: SockJSSocket) {
    ConnectionManager.connect(new SockJsSocketConnection(socket))
  }
}