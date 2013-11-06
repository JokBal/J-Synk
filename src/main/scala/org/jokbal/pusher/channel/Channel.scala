package org.jokbal.pusher.channel

import org.vertx.scala.core.json.{JsonObject,Json,JsonArray}
import org.jokbal.pusher.connection.Connection
import org.vertx.scala.core.eventbus.EventBus


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
}

class Channel{
  def subscribe(connection:Connection,data:JsonObject):Unit
  def unsubscribe(connection:Connection,data:JsonObject):Unit
  def publishEvent[T](event:String,data:T):Boolean
  def sendSubscribeSucceededMessage(connection:Connection,data:JsonObject):Unit
  def isClientTriggerEnabled:Boolean
}
