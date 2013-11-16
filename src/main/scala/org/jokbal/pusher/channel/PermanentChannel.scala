package org.jokbal.pusher.channel

import org.jokbal.pusher.connection.Connection
import org.vertx.scala.core.json._
import scala.collection.mutable
import org.jokbal.pusher.verticle.Pusher
import org.jokbal.pusher.sharedstore.SharedStore

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 11.
 * Time: 오후 5:21
 * To change this template use File | Settings | File Templates.
 */
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

  override def publishEvent(event: String): Boolean = {
    permanentData.getMobile(channelName,handleMobileKeys(event) _)
    super.publishEvent(event)
  }

  val GCM_MATCHER = "GCM:(.*)".r
  def handleMobileKeys(event: String)(keys:JsonArray)
  {
    val gcmKeys= mutable.Buffer[String]()
    val keyArray = keys.toArray
    for(i<-keyArray) i match{
      case GCM_MATCHER(key)=>
        gcmKeys+=key
    }

    val gcmSendData = Json.obj(
      "api_key"->Pusher.gcm_apikey,
      "notification"->Json.obj(
        "data"->event,
        "registration_ids"->gcmKeys)
    )
    Pusher.eventBus.send(Pusher.gcm_address,gcmSendData)
  }


  override def removeMember(connection: Connection){
    userMobileMap-=connection
    super.removeMember(connection)
  }
}
