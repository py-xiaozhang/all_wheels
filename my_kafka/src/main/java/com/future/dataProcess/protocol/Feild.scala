package com.future.dataProcess.protocol

import com.future.dataProcess.`type`.AllType

class Feild(val name:String,
            val tp:AllType,
            val desc:String,
            val defaultData:Any=null,
            val version:Array[Array[Feild]]=null
           ) {

}
