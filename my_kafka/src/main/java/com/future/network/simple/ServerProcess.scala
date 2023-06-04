package com.future.network.simple


import java.nio.channels.{SelectionKey, Selector, ServerSocketChannel, SocketChannel}

class ServerProcess extends Runnable{

  val isClose=false

  var kafkaSelect:KafkaSelect=_
  def process(selectionKey:SelectionKey): Unit={
    kafkaSelect.register(selectionKey)
  }

  override def run(): Unit = {
    kafkaSelect=new KafkaSelect(Selector.open(),1)
    while (!isClose){
      kafkaSelect.processAccept()
      kafkaSelect.selectKey()
    }
  }


}
