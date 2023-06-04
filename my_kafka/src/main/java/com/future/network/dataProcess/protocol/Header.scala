package com.future.network.dataProcess.protocol

import scala.collection.mutable

class Header(map: mutable.HashMap[String,Object]){
    val apiKeys:Int=map.get("api_key").asInstanceOf[Int]
    val apiVersion:Int=map.get("api_version").asInstanceOf[Int]
    val correlationId:Int=map.get("correlation_id").asInstanceOf[Int]
    val clientId:String=map.get("client_id").toString
}