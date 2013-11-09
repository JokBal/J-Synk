package org.jokbal.pusher.verticle

import org.vertx.scala.platform.Verticle
import org.jokbal.pusher.http.HttpServerManager
class HttpServerVerticle extends Verticle{

  override def start(){

    val conf = container.config()
    Pusher.init(conf,vertx.eventBus,vertx.sharedData)
    val test = new HttpServerManager(vertx,conf)
    test.startServer
    println("Succeed to start Http Server Verticle")

  }


}