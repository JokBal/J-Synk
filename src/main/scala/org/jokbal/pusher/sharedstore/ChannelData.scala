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

  def publicChannels(callback:JsonArray=>Unit){
    sharedStore.smembers("public_channels",callback)
  }
  def privateChannels(callback:JsonArray=>Unit){
    sharedStore.smembers("private_channels",callback)
  }
  def presenceChannels(callback:JsonArray=>Unit){
    sharedStore.smembers("presence_channels",callback)
  }
  def Channels(callback:JsonArray=>Unit){
    sharedStore.sunion(callback,"public_channels","private_channels","presence_channels")
  }
}
