package org.jokbal.pusher.channel

import org.jokbal.pusher.connection.Connection
import org.vertx.scala.core.json.JsonObject
import org.jokbal.pusher.util.Encryption
import org.jokbal.pusher.verticle.Pusher

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 3.
 * Time: 오후 8:49
 * To change this template use File | Settings | File Templates.
 */
trait PrivateChannel extends Channel{
  // the auth data for this channel on whole Verticle
  //val authMap = mutable.HashMap[String,JsonObject]()
  override def subscribe(connection:Connection,data:JsonObject){
    if(authUser(connection,data))
      super.subscribe(connection,data)
  }

  def authUser(connection:Connection,data:JsonObject):Boolean=
  {
    return true
    val encryptedSignature = Encryption.hmacsha256Representation(signature(connection,data),Pusher.secret)
    val auth = data.getString("auth")
    auth.equals(Pusher.apikey+":"+encryptedSignature)
  }

  override def isClientTriggerEnabled: Boolean = true
}
