package org.jokbal.pusher.channel

import org.vertx.testtools.VertxAssert._
import org.vertx.scala.testtools._
import org.junit._
import org.vertx.scala.core.json.{JsonObject, Json}
import org.vertx.scala.core.eventbus.{EventBus, MessageData, Message}
import org.jokbal.puhser.verticle.Pusher
import org.jokbal.pusher.connection.Connection
import org.jokbal.pusher.model.Event
import util.WrappedEventBus


/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 9.
 * Time: 오후 6:27
 * To change this template use File | Settings | File Templates.
 */
class PublicChannelTest extends TestVerticle{

  val CHANNEL_NAME = "testChannel"

  @Test
  def subscribeAndUnsubscribeTest(){
    Pusher.init(Json.emptyObj(),vertx.eventBus,vertx.sharedData)
    val mockConnection = new Connection{
      def sendTextFrame(str: String){
        assertEquals(str,Event("pusher_internal:subscription_succeeded",CHANNEL_NAME,"{}").toString)
      }
    }
    val channel = Channel(CHANNEL_NAME).asInstanceOf[BaseChannel]
    channel.subscribe(mockConnection,Json.emptyObj())
    assertTrue(channel.connections.contains(mockConnection))
    channel.unsubscribe(mockConnection)
    assertFalse(channel.connections.contains(mockConnection))
    testComplete()
  }

  @Test
  def publicEventTest(){
    val eventName = "A EventName for Public Channel Test"
    val eventData = "A EventData for Public Channel Test"
    val mockEventBus = new WrappedEventBus("test",vertx.eventBus){
      override def publish[T](address: String, value: T)(implicit evidence$1: (T) => MessageData): EventBus = {
        assertEquals(address,CHANNEL_NAME)
        assertEquals(value.toString,Event(eventName,CHANNEL_NAME,eventData).toString)
        super.publish(address, value)(evidence$1)
      }
    }
    Pusher.init(Json.emptyObj(),vertx.eventBus,vertx.sharedData)
    val mockConnection = new Connection{
      def sendTextFrame(str: String){
        if(str.equals(Event("pusher_internal:subscription_succeeded",CHANNEL_NAME,"{}").toString))
          return
        assertEquals(str,Event(eventName,CHANNEL_NAME,eventData).toString)
        testComplete()
      }
    }
    val channel = Channel(CHANNEL_NAME)
    channel.subscribe(mockConnection,Json.emptyObj())
    channel.publishEvent(eventName,eventData)
  }
}
