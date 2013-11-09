package org.jokbal.pusher.model

import org.vertx.scala.core.json.Json


object Event{
  val SUBSCRIBE = "pusher:subscribe"
  val UNSUBSCRIBE = "pusher:unsubscribe"
  val PING = "pusher:ping"
  val PONG = "pusher:pong"
  val PUSHER_CONNECTION_ESTABLISHED = "pusher:connection_established"
  val CLIENT_EVENT = "(client-.*)".r

  def apply[T](event:String,data:T)=Json.obj("event"->event,"data"->data)
  def apply[T](event:String,channel:String,data:T)=Json.obj("event"->event,"channel"->channel,"data"->data)
  def established(socketId:String)=Event("pusher:connection_established",Json.obj("socket_id"->socketId))
  def error[T](message:String,code:Int)=Event("pusher:error",Json.obj("message"->message,"code"->code))
  def subscribeSuccess[T](channel:String,data:T)=Event("pusher_internal:subscription_succeeded",channel,data.toString)
  def memberAdded[T](channel:String,data:T)=Event("pusher_internal:member_added",channel,data.toString)
  def memberRemoved[T](channel:String,data:T)=Event("pusher_internal:member_removed",channel,data.toString)
  def ping[T]()=Event("pusher:ping", Json.emptyObj.toString)
  def pong[T]()= Event("pusher:pong", Json.emptyObj.toString)
  def internalAuth(socketId:String,channel:String,auth:String)=Event("Internal:subscribeAuth",Json.obj("socketid"->socketId,"channel"->channel,"auth"->auth))
}