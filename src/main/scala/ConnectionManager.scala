import org.vertx.scala.core.http.ServerWebSocket
import org.vertx.scala.core.sockjs.SockJSSocket
import org.vertx.scala.core.buffer.Buffer
import org.vertx.scala.core.json.{JsonObject, JsonArray}

import scala.collection.mutable

import java.util.UUID

class Data(buffer: Buffer) {
  private val jsonObject: JsonObject = new JsonObject(buffer.toString())
  private val dataJsonObject: JsonObject = jsonObject.getObject("data")
  val channelData : JsonObject = dataJsonObject.getObject("channel_data")
  val channel: String = dataJsonObject.getString("channel")
  val auth: String = dataJsonObject.getString("auth")
  val event: String = jsonObject.getString("event")
}

class Connection(socketID: String, webSocket: ServerWebSocket=null, sockJSSocket:SockJSSocket=null) {

  def getSocket: Any = {
    if(webSocket != null) {
      webSocket
    } else if(sockJSSocket != null) {
      sockJSSocket
    }
  }
}

trait ConnectionManager
{
  val connections = mutable.HashMap[String, Connection]()

  def connect(webSocket:ServerWebSocket, sockJSSocket:SockJSSocket=null) = {
    val socketID = UUID.randomUUID().toString
    val connection = new Connection(socketID, webSocket, sockJSSocket)
    connection.getSocket.dataHandler(dataHandler(connection) _)
    connections += socketID->connection
  }

  def dataHandler(connection: Connection)(data:Buffer) {
  }

  def disconnect(connection: Connection) {
    connections -= connection
  }
}
