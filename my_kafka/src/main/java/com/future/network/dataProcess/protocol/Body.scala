package com.future.network.dataProcess.protocol

import scala.collection.mutable

class Body(map: mutable.HashMap[String,Object]){
  val acks=map.get("acks").asInstanceOf[Int]
  val timeout=map.get("timeout").asInstanceOf[Int]
  val topicData=map.get("topic_data")
}
