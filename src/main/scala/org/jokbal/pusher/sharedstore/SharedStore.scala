package org.jokbal.pusher.channel.sharedstore


import org.vertx.scala.core.json.{Json,JsonObject,JsonArray}
import org.jokbal.pusher.sharedstore.ChannelData

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 4.
 * Time: 오후 7:49
 * To change this template use File | Settings | File Templates.
 */
object SharedStore {
  var sharedStore:SharedStore = VertxSharedDataStore
  def enableRedis(){
    sharedStore = RedisStore
  }
  def presenceData(channelName:String) = new PresenceData(channelName,sharedStore)
  def channelData = new ChannelData(sharedStore)
}

class SharedStore{
  def hset(hashName:String,values:(String, String)*){}
  def hdel(hashName:String,keys:String*){}
  def sadd(setName:String,values:String*){}
  def srem(setName:String,values:String*){}
  def hgetall(hashName:String,callback:(JsonObject)=>Unit){}
  def hlen(hashName:String,callback:Int=>Unit){}
  def smembers(setName:String,callback:(JsonArray)=>Unit){}
  def sunion(callback:(JsonArray)=>Unit,setNames:String*){}
}
