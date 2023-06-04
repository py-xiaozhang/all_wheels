package com.future.network.dataProcess.protocol

import java.nio.ByteBuffer

class KafkaRequest(byteBuffer: ByteBuffer) {
  val header=new Header(Protocol.parseHeader(byteBuffer))
  val Body=new Body(Protocol.parseBody(byteBuffer,header.apiKeys,header.apiVersion))
}
