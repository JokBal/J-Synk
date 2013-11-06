package org.jokbal.pusher.channel

import org.vertx.scala.core.json.{JsonObject,Json,JsonArray}
import org.jokbal.pusher.connection.Connection
import org.vertx.scala.core.eventbus.EventBus
import scala.collection.mutable


/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 3.
 * Time: 오후 8:45
 * To change this template use File | Settings | File Templates.
 */

object  Channel{
  var channelPrefix=""
  var eventBus:EventBus=null

  val channelMap = mutable.HashMap[String,Channel]()

  def apply(channelName:String):Channel = {
    if(channelMap.contains(channelName))
      return (channelMap get channelName).get
    makeChannel(channelName)
  }

  val privatePattern = "(private-.*)".r
  val presencePattern = "(presence-.*)".r

  private def makeChannel(channelName:String)={
    val channel = channelName match{
      case privatePattern(c) =>{
        //private channel
        println("private channel Created")
        new BaseChannel(channelName) with PrivateChannel
      }
      case presencePattern(c) => {
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

}

abstract class Channel{
  def subscribe(connection:Connection,data:JsonObject){}
  def unsubscribe(connection:Connection,data:JsonObject){}
  def publishEvent[T](event:String,data:T):Boolean
  def sendSubscribeSucceededMessage(connection:Connection,data:JsonObject){}
  def isClientTriggerEnabled:Boolean
  def signature(connection:Connection,data:JsonObject):String
}
