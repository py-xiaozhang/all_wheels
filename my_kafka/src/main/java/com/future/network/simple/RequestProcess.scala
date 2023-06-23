package com.future.network.simple

import com.future.dataProcess.KafkaApis
import com.future.dataProcess.protocol.KafkaRequest
import com.future.network.io.{KafkaChannel, NetworkReceive}

import java.util
import java.util.Map
import java.util.concurrent.{ArrayBlockingQueue, ConcurrentHashMap}

class RequestProcess(stageReceive:ConcurrentHashMap[KafkaChannel,ArrayBlockingQueue[NetworkReceive]]) extends Runnable{
  var isClose=false
  override def run(): Unit ={
    while (!isClose){
      val entrySetItr: util.Iterator[Map.Entry[KafkaChannel, ArrayBlockingQueue[NetworkReceive]]] = stageReceive.entrySet().iterator()
      while(entrySetItr.hasNext){
        val entry: Map.Entry[KafkaChannel, ArrayBlockingQueue[NetworkReceive]] = entrySetItr.next()
        val channel: KafkaChannel = entry.getKey
        val queue: ArrayBlockingQueue[NetworkReceive] = entry.getValue
        while (!queue.isEmpty){
          val info: String = channel.getTransportLayer().getInfo()
          val receive: NetworkReceive = queue.poll()
          //todo 解析请求
          val request = new KafkaRequest(receive.buffer)
          println("接收到消息："+info+"\n"+request)
          KafkaApis.handle(request)
        }
      }
    }
  }
}
