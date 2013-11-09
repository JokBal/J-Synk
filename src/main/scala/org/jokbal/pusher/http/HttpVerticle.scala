package org.jokbal.pusher.http

import org.vertx.scala.platform.Verticle
import org.jokbal.puhser.verticle.Pusher

class HttpVerticle extends Verticle{

  override def start(){
    super.start()

    val conf = container.config()
    Pusher.init(conf,vertx.eventBus,vertx.sharedData)
    val test = new HttpServerManager(vertx,conf)

    test.startServer()
    System.out.println("Test HttpVerticle Start")

  }


}