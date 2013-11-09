package org.jokbal.pusher.verticle

import org.vertx.scala.platform.Verticle
import org.jokbal.pusher.http.HttpServerManager
import org.jokbal.puhser.verticle.Pusher

class HttpServer extends Verticle{

  override def start(){

    val conf = container.config()
    Pusher.init(conf,vertx.eventBus,vertx.sharedData)
    val test = new HttpServerManager(vertx,conf)

    test.startServer()
  }


}