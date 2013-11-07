package org.jokbal.pusher.channel.sharedstore

import org.vertx.scala.core.json._

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 5.
 * Time: 오후 9:35
 * To change this template use File | Settings | File Templates.
 */
class PresenceData(val channelName:String, val sharedStore:SharedStore){

  def addMember[T](id:String,info:JsonObject){
    sharedStore.hset(channelName,id->info.toString)
  }
  def removeMember(key:String){
    sharedStore.hdel(channelName,key)
  }

  def getPresence(callback:JsonObject=>Unit){
    sharedStore.hgetall(channelName,callback)
  }

  def getPresenceCount(callback:Int=>Unit){
    sharedStore.hlen(channelName,callback)
  }

}
