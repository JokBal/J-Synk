package org.jokbal.pusher.verticle

import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.json.JsonObject
import org.jokbal.pusher.http.HttpServerManager

class HttpServer extends Verticle{

  override def start(){

    val conf = container.config()
    val test = new HttpServerManager(vertx,conf)

    test.startServer()
  }


}