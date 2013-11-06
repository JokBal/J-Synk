package verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.http.ServerWebSocket
import net.{WebSocketConnection, ConnectionManager}

class WebSocketServer extends Verticle {
  override def start() {
    vertx.createHttpServer().websocketHandler(openHandler _) listen(8080)
    println("WebSocket server listening on port 8080")
  }

  def openHandler(socket: ServerWebSocket):Unit = {
    ConnectionManager.connect(new WebSocketConnection(socket))
  }
}