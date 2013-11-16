package org.jokbal.pusher.sharedstore

import org.vertx.scala.core.json._

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 8.
 * Time: 오전 1:34
 * To change this template use File | Settings | File Templates.
 */
class ChannelData(val sharedStore:SharedStore) {

  def addPublicChannels(channelName:String){
    sharedStore.sadd("public_channels",channelName)
  }

  def addPrivateChannels(channelName:String){
    sharedStore.sadd("private_channels",channelName)
  }
  def addPresenceChannels(channelName:String){
    sharedStore.sadd("presence_channels",channelName)
  }
  def addPermanentChannels(channelName:String){
    sharedStore.sadd("permanent_channels",channelName)
  }

  def publicChannels(callback:JsonArray=>Unit){
    sharedStore.smembers("public_channels",callback)
  }
  def privateChannels(callback:JsonArray=>Unit){
    sharedStore.smembers("private_channels",callback)
  }
  def presenceChannels(callback:JsonArray=>Unit){
    sharedStore.smembers("presence_channels",callback)
  }
  def permanentChannels(callback:JsonArray=>Unit){
    sharedStore.smembers("permanent_channels",callback)
  }

  def Channels(callback:JsonArray=>Unit){
    sharedStore.sunion(callback,"public_channels","private_channels","presence_channels","permanent_channels")
  }
}
