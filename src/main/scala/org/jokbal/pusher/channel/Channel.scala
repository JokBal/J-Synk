package org.jokbal.pusher.channel

import org.vertx.scala.core.json.{JsonObject,Json,JsonArray}
import org.jokbal.pusher.connection.Connection
import org.vertx.scala.core.eventbus.EventBus
import scala.collection.mutable
import org.jokbal.pusher.verticle.Pusher
import org.jokbal.pusher.model.Event


/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 3.
 * Time: 오후 8:45
 * To change this template use File | Settings | File Templates.
 */

object  Channel{

  val channelMap = mutable.HashMap[String,Channel]()


  def apply(channelName:String):Channel = {
    if(channelMap.contains(channelName))
      return (channelMap get channelName).get
    makeChannel(channelName)
  }

  private val privatePattern = "(private-.*)".r
  private val presencePattern = "(presence-.*)".r
  private val permanentPattern = "(permanent-.*)".r

  private def makeChannel(channelName:String)={
    val channel = channelName match{

      case Channel.permanentPattern(c) =>{
        println("private channel Created")
        new BaseChannel(channelName) with PermanentChannel with PrivateChannel
      }
      case Channel.privatePattern(c) =>{
        //private channel
        println("private channel Created")
        new BaseChannel(channelName) with PrivateChannel
      }
      case Channel.presencePattern(c) => {
        //presence channel with redis
        println("presence channel Created")
        new BaseChannel(channelName)  with PresenceChannel with PrivateChannel
      }
      case _ =>{
        //public channel
        println("public channel Created")
        new BaseChannel(channelName)
      }
    }
    channelMap+=channelName->channel
    channel
  }

  /**
   * unsubscribe channel
   * @param connection the connection that try to unsubscribe
   * @param channelName name of channel
   */
  def unsubscribeAll(connection:Connection)
  {
    for(channel<-channelMap)
      channel._2.unsubscribe(connection)
  }

  def disconnectConnection(connection:Connection){
    for(channel<-channelMap)
      channel._2.disconnect(connection)
  }


  /**
   * publish event to event bus
   * @param data the wrapped event data
   */
  def publishEvent(channelName:String,data:String)={
    Pusher.eventBus.publish(Pusher.eventBus_prefix+channelName,data)
  }
}

abstract class Channel{
  val channelName:String
  val connections:mutable.Buffer[Connection]
  def subscribe(connection:Connection,data:JsonObject){}
  def unsubscribe(connection:Connection){}
  def disconnect(connection:Connection)
  def publishEvent[T](event:String,data:T):Boolean=publishEvent(Event(event,channelName,data).toString)
  def publishEvent(data:String):Boolean
  def sendSubscribeSucceededMessage(connection:Connection,data:JsonObject){}
  def isClientTriggerEnabled:Boolean
  def signature(connection:Connection,data:JsonObject):String
}
