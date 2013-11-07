package org.jokbal.pusher.channel.sharedstore

import org.vertx.scala.core.json._
import org.vertx.scala.core.shareddata.SharedData
import scala.collection.JavaConversions

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 4.
 * Time: 오후 9:00
 * To change this template use File | Settings | File Templates.
 */
object VertxSharedDataStore extends SharedStore{
  var sharedData:SharedData=null
  override def hset(hashName:String,values:(String, String)*){
    val map = JavaConversions.mapAsScalaConcurrentMap[String,String](sharedData.getMap(hashName))
    for(tuple<-values){
      map += tuple
    }
  }
  override def hdel(hashName:String,keys:String*){
    val map = sharedData.getMap(hashName)
    for(key<-keys){
      map.remove(key)
    }
  }
  override def sadd(setName:String,values:String*){
    val set = sharedData.getSet[String](setName)
    for(value<-values){
      set.add(value)
    }
  }
  override def srem(setName:String,values:String*){
    val set = sharedData.getSet(setName)
    for(value<-values){
      set.remove(value)
    }
  }
  override def hgetall(hashName:String,callback:JsonObject=>Unit){
    val result = Json.emptyObj()
    val map =JavaConversions.mapAsScalaConcurrentMap(sharedData.getMap(hashName))
    for(tuple<-map){
      result.putString(tuple._1,tuple._2)
    }
    callback(result)
  }

  override def hlen(hashName:String,callback:Int=>Unit){
    callback(sharedData.getMap(hashName).size())
  }
  override def smembers(setName:String,callback:JsonArray=>Unit){
    val result = Json.emptyArr()
    val set = JavaConversions.asScalaSet(sharedData.getSet(setName))
    for(item<-set)
    {
      result.add(item)
    }
  }
  override def sunion(callback:(JsonArray)=>Unit,setNames:String*){
    val result = Json.emptyArr()
    for(setName<-setNames){
      val set = JavaConversions.asScalaSet(sharedData.getSet(setName))
      for(item<-set)
      {
        result.add(item)
      }
    }
    callback(result)
  }

}
