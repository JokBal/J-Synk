package org.jokbal.pusher.channel

import org.jokbal.pusher.connection.Connection
import org.vertx.scala.core.json._
import scala.collection.mutable
import org.jokbal.pusher.verticle.Pusher
import org.jokbal.pusher.sharedstore.SharedStore
import org.vertx.scala.core.eventbus.Message

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 11.
 * Time: 오후 5:21
 * To change this template use File | Settings | File Templates.
 */

object PermanentChannel {
  lazy val permanentData = SharedStore.permanentData
  def publishPermanentEvent(channelName:String, event: String){
    println("publish permanent    "+channelName+"      "+event)
    permanentData.getMobile(channelName,handleMobileKeys(event))
  }

  val GCM_MATCHER = "GCM:(.*)".r
  def handleMobileKeys(event: String)(keys:JsonArray)
  {
    val gcmKeys= Json.emptyArr()
    for(i<-Range(0,keys.size())) keys.get[JsonObject](i).getString("mobile") match{
      case GCM_MATCHER(key)=>
        gcmKeys addString key
    }

    val jsonEvent =Json.fromObjectString(event)

    val gcmSendData = Json.obj(
      "data"->jsonEvent,
      "registration_ids"->gcmKeys)

    Pusher.eventBus.send(Pusher.gcm_address,gcmSendData,
    { msg:Message[JsonObject]=>
      println(msg.body.toString)
    })
  }

}

trait PermanentChannel extends PresenceChannel{

  val userMobileMap = mutable.HashMap[Connection,String]()
  val permanentData = SharedStore.permanentData

  override def addMember(connection: Connection, data:JsonObject){
    val mobile = data.getString("mobile")
    userMobileMap+=connection->mobile
    permanentData.deleteMobile(channelName,mobile)
    data.removeField("mobile")
    super.addMember(connection,data)
  }

  override def disconnect(connection: Connection){
    val mobile = userMobileMap get connection
    userMobileMap -= connection
    connections -= connection
    userIdMap -= connection
    if(mobile.isEmpty)return
    addOfflineConnection(mobile.get)
  }

  def addOfflineConnection(mobile:String){
    permanentData.insertMobile(channelName,mobile)
  }
    /*
  override def publishEvent(event: String): Boolean = {
    PermanentChannel.publishPermanentEvent(channelName,event)
    super.publishEvent(event)
  }    */

  override def removeMember(connection: Connection){
    userMobileMap-=connection
    super.removeMember(connection)
  }
}
