package org.jokbal.pusher.channel

import org.jokbal.pusher.connection.Connection
import scala.collection.{JavaConversions, mutable}
import org.vertx.scala.core.json._
import org.jokbal.pusher.sharedstore.SharedStore
import org.jokbal.pusher.model.Event

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 3.
 * Time: 오후 8:50
 * To change this template use File | Settings | File Templates.
 */
trait PresenceChannel extends BaseChannel{

  val userIdMap = mutable.HashMap[Connection,String]()
  val presenceStore = SharedStore.presenceData(channelName)

  override def subscribe(connection:Connection,data:JsonObject){
    val channel_data= data.getObject("channel_data")
    addMember(connection,channel_data)
    super.subscribe(connection,data)
  }
  override def unsubscribe(connection:Connection){
    removeMember(connection)
    super.unsubscribe(connection)
  }

  override def sendSubscribeSucceededMessage(connection: Connection, data: JsonObject=Json.emptyObj())
  {
    presenceStore.getPresence(sendPresenceData(connection,data))
  }

  def addMember(connection:Connection,data:JsonObject)
  {
    val id = data.getString("user_id")
    val info = data.getObject("user_info")
    presenceStore.addMember(id,info)
    userIdMap+=connection->id
    publishEvent(Event.memberAdded(channelName,data).toString)
  }

  def removeMember(connection:Connection)
  {
    val data =Json.fromObjectString((userIdMap get connection).get)
    presenceStore.removeMember(data.getString("user_id"))
    publishEvent(Event.memberRemoved(channelName,data).toString)
    userIdMap-=connection
  }

  def sendPresenceData(connection: Connection, data: JsonObject)(hash:JsonObject)
  {
    val ids:JsonArray = new JsonArray
    for(i<-JavaConversions.mapAsScalaMap(hash.toMap)){
      ids add i._1
    }
    val jsonData = Json.obj("ids"->ids,"hash"->hash,"count"->ids.size)
    data.putObject("presence",jsonData)
    super.sendSubscribeSucceededMessage(connection,data)
  }

  override def signature(connection:Connection,data:JsonObject)=
    connection.socketId+":"+channelName+":"+data.getObject("channel_data").toString

}
