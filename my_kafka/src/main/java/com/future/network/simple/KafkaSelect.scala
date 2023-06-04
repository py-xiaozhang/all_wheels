package com.future.network.simple

import com.future.network.io.{KafkaChannel, NetworkReceive, TransportLayer}

import java.nio.channels.{SelectionKey, Selector, ServerSocketChannel, SocketChannel}
import java.util
import java.util.concurrent.{ArrayBlockingQueue, ConcurrentHashMap}

class KafkaSelect(selector: Selector,requestProessNums:Int) {
  var stageReceive:ConcurrentHashMap[KafkaChannel,ArrayBlockingQueue[NetworkReceive]]=new ConcurrentHashMap[KafkaChannel,ArrayBlockingQueue[NetworkReceive]]()
  val channels:ArrayBlockingQueue[SocketChannel] =new ArrayBlockingQueue[SocketChannel](10000)
  var requestProess:Array[Thread]=_

  init()

  def init()={
    requestProess=Array.fill(requestProessNums){
      new Thread(new RequestProess(stageReceive))
    }
    for(i <-Range(0,requestProessNums)){
      requestProess(i).start()
    }
  }


  def register(selectkey: SelectionKey): Unit ={
    val serverSocketChannel: ServerSocketChannel = selectkey.channel().asInstanceOf[ServerSocketChannel]

    //连接对象信息
    val channel: SocketChannel = serverSocketChannel.accept()
    channels.put(channel)
  }

  def processAccept(): Unit ={
    while (!channels.isEmpty){
      val channel: SocketChannel = channels.poll()
      channel.configureBlocking(false)
      val key: SelectionKey = channel.register(selector, SelectionKey.OP_READ)
      val layer = new TransportLayer(key, channel)
      val kafkaChannel =new KafkaChannel(key.hashCode(),layer);
      //进行绑定
      key.attach(kafkaChannel)
    }
  }


  def selectKey()={
    selector.select(3000)
    val keys: util.Set[SelectionKey] = selector.selectedKeys()
    val itr: util.Iterator[SelectionKey] = keys.iterator()
    processSelectKey(itr)
  }

  private def processSelectKey(itr: util.Iterator[SelectionKey])={
    while(itr.hasNext){
      val key: SelectionKey = itr.next()
      if(key.isReadable){
        val channel:KafkaChannel  = key.attachment().asInstanceOf[KafkaChannel]
        var receive: NetworkReceive=new NetworkReceive
        while (receive!=null){
          try {
            receive = channel.read()
            if(receive!=null){
              addStageReceive(channel, receive)
            }
          }catch {
            case e:Exception=>{
              println(e)
              channel.close()
              stageReceive.remove(channel)
            }
              receive=null
          }

        }

      }
      itr.remove()
    }
  }

  private def addStageReceive(channel:KafkaChannel,receive: NetworkReceive)={
    val deque: ArrayBlockingQueue[NetworkReceive] = stageReceive.getOrDefault(channel, new ArrayBlockingQueue[NetworkReceive](10000))
    deque.put(receive)
    stageReceive.put(channel,deque)
  }


}
