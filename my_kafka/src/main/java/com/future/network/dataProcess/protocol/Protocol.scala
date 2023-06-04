package com.future.network.dataProcess.protocol

import com.future.network.dataProcess.`type`.AllType

import java.nio.ByteBuffer
import scala.collection.mutable

class Protocol {
  val request_header:Array[Feild]=Array(
    new Feild("api_key",AllType.INT16,"request api for do method"),
    new Feild("api_version",AllType.INT16,"protocol version"),
    new Feild("correlation_id",AllType.INT32,"client id for response"),
    new Feild("client_id",AllType.NULLABLE_STRING,"A user specified identifier for the client making the request")
  )

  val body:Array[Array[Array[Feild]]]=Array.fill(21){
    null
  }

  //todo 暂时只实现Produce vo版本
  body(0)=Array(
    //todo V0版本
    Array(
      new Feild("acks",AllType.INT16,""),
      new Feild("timeout",AllType.INT32,""),
      new Feild("topic_data",null,"client id for response",null,
        Array(
          //todo V0版本
          Array(
            new Feild("topic",AllType.NULLABLE_STRING,""),
            new Feild("data",null,"",null,
              Array(
                //todo V0版本
                Array(
                  new Feild("partition",AllType.INT32,""),
                  new Feild("record_set",AllType.NULLABLE_STRING,"")
                )
              )
            )
          )
        )
      ),
    )
  )
}

object Protocol{
  val protocol=new Protocol

  def parseHeader(Bytebuffer:ByteBuffer): mutable.HashMap[String,Object] ={
    val map =new mutable.HashMap[String,Object]();
    for (f <- protocol.request_header) {
      map.put(f.name,f.tp.parse(Bytebuffer))
    }
    map
  }

  def parseBody(byteBuffer:ByteBuffer,apikey:Int,version:Int): mutable.HashMap[String,Object] ={
    val map =new mutable.HashMap[String,Object]();
    for (f <- protocol.body(apikey)(version)) {
      map.put(f.name,dfs(byteBuffer,version,f))
    }
    map
  }

  def dfs(byteBuffer :ByteBuffer,version:Int,feild:Feild): Object ={
    if(feild.tp==null){
      val feilds: Array[Feild] = feild.version(version)
      if(feilds!=null){
        val map =new mutable.HashMap[String,Object]();
        for (f <- feilds) {
          map.put(f.name,dfs(byteBuffer,version,f))
        }
        map
      }else{
        null
      }
    }else{
      feild.tp.parse(byteBuffer)
    }
  }

  def main(args: Array[String]): Unit = {
    /** kafka vo版本请求协议
     * ---------------------------------------------------------------------
     * size                                                         (4Byte)
     * ----------------------------------------------------------------------
     *                    api_key                                   (2Byte)
     *                --------------------------------------------------------
     *                    api_version                               (2Byte)
     * request_header---------------------------------------------------------
     *                    correlation_id                            (3Byte)
     *                --------------------------------------------------------
     *                    client_id                                 (4Byte+具体大小)
     *------------------------------------------------------------------------
     *                       acks                                   (2Byte)
     *                  ------------------------------------------------------
     *                       timeout                                (4Byte)
     *                  ------------------------------------------------------
     * request_body                     |  topic                    (4Byte+具体大小)
     *                                  |--------------------------------------
     *                      topic_data  |         | partition       (4Byte)
     *                                  |  data -------------------------------
     *                                  |         | record_set      (4Byte+具体大小)
     * -------------------------------------------------------------------------
     */


    val byteBuffer:ByteBuffer =ByteBuffer.allocate(1024)

    byteBuffer.putShort(1)
    byteBuffer.putInt(600000)

    val topic:String="hello world-0"
    val bytes: Array[Byte] = topic.getBytes
    byteBuffer.putInt(bytes.length)
    byteBuffer.put(bytes)

    byteBuffer.putInt(0)

    val data:String="data 6666"
    val dataBytes: Array[Byte] = data.getBytes
    byteBuffer.putInt(dataBytes.length)
    byteBuffer.put(dataBytes)

    byteBuffer.flip()

    val map: mutable.HashMap[String, Object] = parseBody(byteBuffer, 0, 0)
    println(map)
  }
}
