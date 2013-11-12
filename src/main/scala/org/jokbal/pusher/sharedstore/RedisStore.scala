package org.jokbal.pusher.sharedstore

import org.vertx.scala.core.json._
import org.jokbal.pusher.verticle.Pusher
import org.vertx.scala.core.eventbus.Message

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 4.
 * Time: 오후 9:02
 * To change this template use File | Settings | File Templates.
 */
object RedisStore extends SharedStore{
  var redisAddress:String = "io.vertx.redis"

  override def hset(hashName:String,values:(String, String)*){
    val args = Json.arr(hashName)
    for(tuple<-values){
      args.addString(tuple._1)
      args.addString(tuple._2)
    }
    val command = Json.obj("command"->"hset","args"->args)
    Pusher.eventBus.internal.send(redisAddress,command)
  }
  override def hdel(hashName:String,keys:String*){
    val args = Json.arr(hashName)
    for(key<-keys){
      args.addString(key)
    }
    val command = Json.obj("command"->"hdel","args"->args)
    Pusher.eventBus.internal.send(redisAddress,command)
  }
  override def sadd(setName:String,values:String*){
    val args = Json.arr(setName)
    for(value<-values){
      args.addString(value)
    }
    val command = Json.obj("command"->"sadd","args"->args)
    Pusher.eventBus.internal.send(redisAddress,command)
  }
  override def srem(setName:String,values:String*){
    val args = Json.arr(setName)
    for(value<-values){
      args.addString(value)
    }
    val command = Json.obj("command"->"srem","args"->args)
    Pusher.eventBus.internal.send(redisAddress,command)
  }
  override def hgetall(hashName:String,callback:(JsonObject)=>Unit){
    val args = Json.arr(hashName)
    val command = Json.obj("command"->"hgetall","args"->args)
    Pusher.eventBus.internal.send[JsonObject](redisAddress,command,
    {msg:Message[JsonObject] =>
      callback(msg.body.getObject("value"))
    })
  }
  override def hlen(hashName:String,callback:Int=>Unit){
    val args = Json.arr(hashName)
    val command = Json.obj("command"->"hlen","args"->args)
    Pusher.eventBus.internal.send[JsonObject](redisAddress,command,
    {msg:Message[JsonObject] =>
      callback(msg.body.getInteger("value"))
    })
  }
  override def smembers(setName:String,callback:(JsonArray)=>Unit){
    val args = Json.arr(setName)
    val command = Json.obj("command"->"smembers","args"->args)
    Pusher.eventBus.internal.send[JsonObject](redisAddress,command,
    {msg:Message[JsonObject] =>
      callback(msg.body.getArray("value"))
    })
  }
  override def sunion(callback:(JsonArray)=>Unit,setNames:String*){
    val args = Json.arr(setNames)
    val command = Json.obj("command"->"sunion","args"->args)
    Pusher.eventBus.internal.send[JsonObject](redisAddress,command,
    {msg:Message[JsonObject] =>
      callback(msg.body.getArray("value"))
    })
  }
}
