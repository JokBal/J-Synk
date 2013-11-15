package org.jokbal.pusher.sharedstore

import org.vertx.scala.core.json._
import org.vertx.scala.core.shareddata.SharedData
import scala.collection.JavaConversions
import org.jokbal.pusher.verticle.Pusher

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 4.
 * Time: 오후 9:00
 * To change this template use File | Settings | File Templates.
 */
object VertxSharedDataStore extends SharedStore{

  lazy val sharedData:SharedData = Pusher.sharedData
  override def hset(hashName:String,values:(String, String)*){
    val map = JavaConversions.mapAsScalaConcurrentMap[String,String](sharedData.getMap(hashName))
    for(tuple<-values){
      map += tuple
    }
  }
  override def hdel(hashName:String,keys:String*){
    val map = JavaConversions.mapAsScalaMap(sharedData.getMap[String,String](hashName))
    for(key<-keys){
      map-=key
    }
  }
  override def sadd(setName:String,values:String*){
    val set = sharedData.getSet[String](setName)
    for(value<-values){
      set.add(value)
    }
  }
  override def srem(setName:String,values:String*){
    val set = JavaConversions.asScalaSet(sharedData.getSet[String](setName))
    for(value<-values){
      set-=value
    }
  }
  override def hgetall(hashName:String,callback:JsonObject=>Unit){
    val result = Json.emptyObj()
    val map =JavaConversions.mapAsScalaConcurrentMap(sharedData.getMap[String,String](hashName))
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
    val set = JavaConversions.asScalaSet(sharedData.getSet[String](setName))
    for(item<-set)
    {
      result.add(item)
    }
    callback(result)
  }
  override def sunion(callback:(JsonArray)=>Unit,setNames:String*){
    val result = Json.emptyArr()
    for(setName<-setNames){
      val set = JavaConversions.asScalaSet(sharedData.getSet[String](setName))
      for(item<-set)
      {
        result.add(item)
      }
    }
    callback(result)
  }

}
