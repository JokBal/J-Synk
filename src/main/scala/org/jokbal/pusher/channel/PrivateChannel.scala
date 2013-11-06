package org.jokbal.pusher.channel

import org.jokbal.pusher.connection.Connection
import org.vertx.scala.core.json._
import org.vertx.scala.core.eventbus.Message

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
  def subscribe(connection:Connection,data:JsonObject){

  }

  def authUser(connection:Connection,data:JsonObject)
  {
    /* TODO
    val authEvent =Event.internalAuth(connection.socketid,channelName,auth)
    val handler = authCallback(connection,auth,data) _
    eventBus.send(Pusher.authorizationChannel,authEvent,handler)*/
  }

  /**
   * The callback method of authorization request.
   * @param connection the connection that try to subscribe this channel
   * @param auth the auth information
   * @param data other information for subscribe
   * @param msg reply message from authorization module
   */
  def authCallback(connection:Connection,auth:String, data:JsonObject)(msg:Message[JsonObject])
  {
    /*TODO
    val result =msg.body.getBoolean("result")
    val authData = msg.body.getObject("data")
    if(result)
    {
      authMap+=connection.socketid->authData
      super.subscribe(connection,auth,data)
      return
    }
    subscriptionFiled(connection,"authorization failed")
    */
  }
}
