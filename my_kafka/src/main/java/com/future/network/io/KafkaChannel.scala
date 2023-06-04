package com.future.network.io

import java.nio.ByteBuffer

class KafkaChannel(id:Int,transportLayer: TransportLayer) {

  def read():NetworkReceive={
    transportLayer.read()
  }

  def getTransportLayer() : TransportLayer={
    transportLayer
  }

  def close(): Unit ={
    transportLayer.close()
  }
}
