package com.future.dataProcess.protocol

import java.nio.ByteBuffer
import scala.collection.mutable

class KafkaRequest(byteBuffer: ByteBuffer) {
  val header=new Header(Protocol.parseHeader(byteBuffer))
//  val Body= new ProduceBody(Protocol.parseBody(byteBuffer,header.apiKeys,header.apiVersion))
  private val bodyMap: mutable.HashMap[String, Object] = Protocol.parseBody(byteBuffer, header.apiKeys, header.apiVersion)
  val Body= header.apiKeys match {
    case  ApiKeys.PRODUCE.id=>new ProduceBody(bodyMap)
    case ApiKeys.FETCH.id=>new FetchBody(bodyMap)
  }
  override def toString = s"KafkaRequest($header, $Body)"
}
