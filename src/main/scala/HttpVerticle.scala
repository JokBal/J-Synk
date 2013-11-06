package org.jokbal.pusher
import org.vertx.scala.platform.Verticle
import org.vertx.scala.core.json.JsonObject

class HttpVerticle extends Verticle{

  override def start(){
    super.start()

    val conf = new JsonObject()
    conf.putNumber("server_port",9999)
    conf.putString("server_enabled","true")

    val test = new HttpServerManager(vertx,conf)

    test.startServer()
    System.out.println("Test HttpVerticle Start")

  }


}