package util

import org.vertx.scala.core.eventbus.{MessageData, EventBus}
import org.vertx.scala.core.eventbus
import org.vertx.java.core.eventbus.Message

/**
 * Created with IntelliJ IDEA.
 * User: infinitu
 * Date: 2013. 11. 9.
 * Time: 오후 4:29
 * To change this template use File | Settings | File Templates.
 */
class WrappedEventBus(val prefix:String, val internal:EventBus){
  def publish[T](address: String, value: T)(implicit evidence$1: (T) => MessageData): EventBus = internal.publish(prefix+address, value)(evidence$1)

  def registerHandler[T](address: String, handler: (eventbus.Message[T]) => Unit)(implicit evidence$4: (T) => MessageData): EventBus = internal.registerHandler(prefix+address, handler)(evidence$4)

  def registerHandler[T](address: String, handler: (eventbus.Message[T]) => Unit, resultHandler: (_root_.org.vertx.scala.core.AsyncResult[Void]) => Unit)(implicit evidence$5: (T) => MessageData): EventBus = internal.registerHandler(prefix+address, handler, resultHandler)(evidence$5)

  def registerLocalHandler[T](address: String, handler: (eventbus.Message[T]) => Unit)(implicit evidence$6: (T) => MessageData): EventBus = internal.registerLocalHandler(prefix+address, handler)(evidence$6)

  def registerUnregisterableHandler[X](address: String, handler: _root_.org.vertx.scala.core.Handler[Message[X]]): EventBus = internal.registerUnregisterableHandler(prefix+address, handler)

  def registerUnregisterableHandler[X](address: String, handler: _root_.org.vertx.scala.core.Handler[Message[X]], resultHandler: (_root_.org.vertx.scala.core.AsyncResult[Void]) => Unit): EventBus = internal.registerUnregisterableHandler(prefix+address, handler, resultHandler)

  def send[T](address: String, value: T)(implicit evidence$2: (T) => MessageData): EventBus = internal.send(prefix+address, value)(evidence$2)

  def send[T](address: String, value: T, handler: (eventbus.Message[T]) => Unit)(implicit evidence$3: (T) => MessageData): EventBus = internal.send(prefix+address, value, handler)(evidence$3)

  def unregisterHandler[T](address: String, handler: _root_.org.vertx.scala.core.Handler[Message[T]])(implicit evidence$7: (T) => MessageData): EventBus = internal.unregisterHandler(prefix+address, handler)(evidence$7)

  def unregisterHandler[T](address: String, handler: _root_.org.vertx.scala.core.Handler[Message[T]], resultHandler: (_root_.org.vertx.scala.core.AsyncResult[Void]) => Unit)(implicit evidence$8: (T) => MessageData): EventBus = internal.unregisterHandler(prefix+address, handler, resultHandler)(evidence$8)
}
