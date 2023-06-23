package com.future.dataProcess.protocol

import scala.collection.mutable

class Header(map: mutable.HashMap[String,Object]){
    val apiKeys:Int=map("api_key").asInstanceOf[Short]
    val apiVersion:Int=map("api_version").asInstanceOf[Short]
    val correlationId:Int=map("correlation_id").asInstanceOf[Int]
    val clientId:String=map("client_id").toString
}