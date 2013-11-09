package org.jokbal.pusher.verticle

import org.vertx.scala.platform.Verticle
<<<<<<< HEAD:src/main/scala/org/jokbal/pusher/verticle/HttpServer.scala
import org.vertx.scala.core.json.JsonObject
import org.jokbal.pusher.http.HttpServerManager
=======
import org.jokbal.puhser.verticle.Pusher
>>>>>>> eebb11213d599f650903c138239c808e5a08fc62:src/main/scala/org/jokbal/pusher/http/HttpVerticle.scala

class HttpServer extends Verticle{

  override def start(){

    val conf = container.config()
    Pusher.init(conf,vertx.eventBus,vertx.sharedData)
    val test = new HttpServerManager(vertx,conf)

    test.startServer()
  }


}