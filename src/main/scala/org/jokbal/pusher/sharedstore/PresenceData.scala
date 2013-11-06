package org.jokbal.pusher.channel.sharedstore

import org.vertx.scala.core.json._

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 5.
 * Time: 오후 9:35
 * To change this template use File | Settings | File Templates.
 */
class PresenceData(val channelName, val sharedStore:SharedStore){

  def addMember[T](data:JsonObject){
    val id = data.getString("user_id")
    val info = data.getObject("user_info")
    sharedStore.hset(channelName,key->data.toString)
  }
  def delMember(key:String){
    sharedStore.hdel(channelName,key)
  }

  def PresenceData(callback:JsonObject=>Unit){
    sharedStore.hgetall(channelName,callback)
  }

}
