package org.jokbal.pusher.channel

import scala.collection.mutable
import org.vertx.scala.core.json.{JsonObject,Json,JsonArray}
import org.vertx.scala.core.eventbus.Message
import org.jokbal.pusher.connection.Connection
import org.jokbal.pusher.model.Event
import org.jokbal.pusher.verticle.Pusher

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 3.
 * Time: 오후 8:49
 * To change this template use File | Settings | File Templates.
 */
class BaseChannel(val channelName:String) extends Channel{
  // the connections that subscribe this channel
  val connections = new mutable.ArrayBuffer[Connection] with mutable.SynchronizedBuffer[Connection]
  Pusher.eventBus.registerHandler(Pusher.eventBus_prefix+channelName,handleEvent _)

  /**
   * subscribe this channel
   * @param connection the connection that try to subscribe this channel
   * @param data information for subscribe
   */
  override def subscribe(connection:Connection, data:JsonObject){
    sendSubscribeSucceededMessage(connection,new JsonObject)
    connections+=connection
  }

  /**
   * unsubscribe this channel
   * @param connection the connection that try to unsubscribe
   */
  override def unsubscribe(connection:Connection){
    connections-=connection
  }

  override def disconnect(connection:Connection){
    unsubscribe(connection)
  }
  def handleEvent(msg:Message[String]){
    handleEvent(msg.body)
  }

  def handleEvent(event:String){
    //println("Event Handled event = " + event.toString)

    for(connection <- connections)
    {
      //println(connection.socketId)
      connection.sendTextFrame(event.toString)
    }
  }

  override def publishEvent(data:String):Boolean={
    Channel.publishEvent(channelName,data)
    true
  }

  override def sendSubscribeSucceededMessage(connection:Connection,data:JsonObject=Json.emptyObj()){
    connection.sendTextFrame(Event.subscribeSuccess(channelName,data).toString)
  }

  override def isClientTriggerEnabled = false

  override def signature(connection:Connection,data:JsonObject)=connection.socketId+":"+channelName
}
