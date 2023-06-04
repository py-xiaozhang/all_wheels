package com.future.network.io

import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

class NetworkReceive {
  //发送数据长度读取
  val size:ByteBuffer=ByteBuffer.allocate(4)
  //接收数据
  var buffer:ByteBuffer=_
  var maxLength:Int=0xffffffff

  def read(socketChannel: SocketChannel): Int ={
    var bts=0;
    if(size.hasRemaining){
      val bt: Int = socketChannel.read(size)
      bts+=bt
      if(bt<0){
        throw new Exception("读取数据异常！")
      }
      if(!size.hasRemaining){
        //重置到0位置
        size.rewind()
        val length: Int = size.getInt
        if(maxLength != -1&& length>maxLength){
          throw new Exception("数据过大异常！")
        }
        buffer=ByteBuffer.allocate(length)
      }
    }

    if(buffer!=null){
      val bytesRead=socketChannel.read(buffer)
      if (bytesRead<0){
        throw new Exception("读取数据异常！")
      }
      bts+=bytesRead
    }
    bts
  }

  def isReadComplete():Boolean={
    !size.hasRemaining && !buffer.hasRemaining
  }
}
