package net

import scala.collection.mutable

object ConnectionManager {
  val connections = mutable.HashMap[String, Connection]()

  def connect(connection: Connection) {
    connections += connection.socketId->connection
  }

  def disconnect(socketId: String) {
    connections -= socketId
  }
}

