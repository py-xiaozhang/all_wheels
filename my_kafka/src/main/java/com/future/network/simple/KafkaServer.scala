package com.future.network.simple

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.{SelectableChannel, SelectionKey, Selector, ServerSocketChannel, SocketChannel}
import java.util

object KafkaServer {

  var processHandler:Array[ServerProcess]=_
  var selector: Selector=_

  def main(args: Array[String]): Unit = {
    init()
    println("kafka初始化完成!")
    while (true){
      selector.select()
      val keys: util.Set[SelectionKey] = selector.selectedKeys()
      val itr: util.Iterator[SelectionKey] = keys.iterator()
      while(itr.hasNext){
        val key: SelectionKey = itr.next()
        if (key.isAcceptable){
          processHandler(0).process(key)
        }else if (key.isReadable){
          val channel1: SocketChannel = key.channel().asInstanceOf[SocketChannel]
          val buffer: ByteBuffer = ByteBuffer.allocate(1024)
          val size: Int = channel1.read(buffer)
          if(size>0){
            println(buffer.hasRemaining)
            println(new String(buffer.array(),0,size))
            println(buffer.hasRemaining)
          }
        }
        itr.remove()
      }
    }

  }

  def init(): Unit ={
    processHandler=Array(new ServerProcess)
    new Thread(processHandler(0)).start()
    val channel: ServerSocketChannel = ServerSocketChannel.open()
    channel.configureBlocking(false)
    channel.socket().bind(new InetSocketAddress("127.0.0.1",6666))
    selector= Selector.open()
    channel.register(selector,SelectionKey.OP_ACCEPT)
  }
}
