package org.jokbal.pusher.http

import org.vertx.scala.core.http.HttpServerRequest

class ChannelTrigger(req : HttpServerRequest){


}

trait Channels{

  def get(req : HttpServerRequest) = {

    val filter_by_prefix = req.params().get("filter_by_prefix")
    val info = req.params().get("info")




  }

  def infoAttribute(info : String) = {
    info.split(",")
  }

  def getFilteredList(filter : String) = {


  }


}

trait Channel{

  def get(req : HttpServerRequest) = {


  }




}

trait Users{

  def get(req : HttpServerRequest) = {


  }





}