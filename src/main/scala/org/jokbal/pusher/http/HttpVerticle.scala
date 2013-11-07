package org.jokbal.pusher.http

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.json.JsonObject

class HttpVerticle extends Verticle{

  override def start(){
    super.start()

    val conf = container.config()
    val test = new HttpServerManager(vertx,conf)

    test.startServer()
    System.out.println("Test HttpVerticle Start")

  }


}