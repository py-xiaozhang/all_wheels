package com.future.network.io

import java.nio.channels.{SelectionKey, SocketChannel}

class TransportLayer(key:SelectionKey,socketChannel:SocketChannel) {
  var networkReceive:NetworkReceive=new NetworkReceive
  def read() :NetworkReceive={
    var tmp:NetworkReceive=null
    if(networkReceive==null){
      networkReceive=new NetworkReceive
    }
    networkReceive.read(socketChannel)
    if(networkReceive.isReadComplete()){
      //重置buffer,从0位置开始读取数据
      networkReceive.buffer.rewind()
      tmp=networkReceive
      networkReceive=null
    }
    tmp
  }

  def getInfo(): String ={
    val localHost = socketChannel.socket().getLocalAddress.getHostAddress
    val localPort = socketChannel.socket().getLocalPort
    val remoteHost = socketChannel.socket().getInetAddress.getHostAddress
    val remotePort = socketChannel.socket().getPort
    remoteHost+":"+remotePort+"-->"+localHost+":"+localPort
  }

  def close(): Unit ={
    try {
      socketChannel.socket.close()
      socketChannel.close()
    } finally {
      key.attach(null)
      key.cancel()
    }
  }
}
