package org.jokbal.pusher.channel

import org.jokbal.pusher.connection.Connection
import scala.collection.mutable
import org.vertx.scala.core.json._
import org.jokbal.pusher.Event
import org.jokbal.pusher.channel.sharedstore.SharedStore

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 3.
 * Time: 오후 8:50
 * To change this template use File | Settings | File Templates.
 */
trait PresenceChannel extends Channel{

  val userIdMap = mutable.HashMap[Connection,String]()
  val presenceData = SharedStore.presenceData(channelName)

  override def subscribe(connection:Connection,data:JsonObject){
    val channel_data= data.getObject(channelData)
    addMember(connection,channel_data)
    super.subscribe(connection,data)
  }
  override def unsubscribe(connection:Connection,data:JsonObject){
    removeMember(connection)
    super.unsubscribe(connection,data)
  }

  override def sendSubscribeSuccessMessage(connection: Connection, data: JsonObject=Json.emptyObj())
  {
    presenceData(sendPresenceData(connection,data))
  }

  def addMember(connection:Connection,data:JsonObject)
  {
    val id = data.getString("user_id")
    val info = data.getObject("user_info")
    presenceData.addMember(id,info)
    userIdMap+=connection->id
    publishEvent(Event.memberAdded(channelName,data).toString)
  }

  def removeMember(connection:Connection)
  {
    presenceData.removeMember((userIdMap get connection).get)
    userIdMap-=connection
    publishEvent(Event.memberRemoved(channelName,data).toString)
  }

  def sendPresenceData(connection: Connection, data: JsonObject)(presence:JsonObject)
  {
    data.putObject("presence",presence)
    super.sendSubscribeSuccessMessage(connection,data)
  }

}
