package com.future.dataProcess.protocol

import scala.collection.mutable

class ProduceBody(map: mutable.HashMap[String,Object]){
  val acks=map("acks").asInstanceOf[Short]
  val timeout=map("timeout").asInstanceOf[Int]
  val topicData=map("topic_data").asInstanceOf[mutable.HashMap[String,Object]]
  val topicPartition=new TopicPartition(topicData)

  override def toString = s"ProduceBody($acks, $timeout, $topicData, $topicPartition)"
}
