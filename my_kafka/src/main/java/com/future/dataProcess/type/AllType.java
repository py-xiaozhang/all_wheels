package com.future.dataProcess.type;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public enum AllType {
    INT16(2,"INT16","",Short.class){
        @Override
        public Object getObj(String obj) {
            return Short.parseShort(obj);
        }
    },
    INT32(4,"INT32","",Integer.class){
        @Override
        public Object getObj(String obj) {
            return Integer.parseInt(obj);
        }
    },
    INT64(8,"INT64","",Long.class){
        @Override
        public Object getObj(String obj) {
            return Long.parseLong(obj);
        }
    },
    NULLABLE_STRING(4,"NULLABLE_STRING","",String.class){
        @Override
        public Object parse(ByteBuffer buffer) {
            int length = buffer.getInt();
            byte[] data =new byte[length];
            buffer.get(data);
            return new String(data, StandardCharsets.UTF_8);
        }
    },
    ;
    private int bytes;
    private String name;
    private String desc;
    private Class aClass;
    AllType(int bytes, String name, String desc,Class clazz){
        this.bytes=bytes;
        this.name=name;
        this.desc=desc;
        this.aClass=clazz;
    }
    public Object parse(ByteBuffer buffer){
        byte[] data=new byte[bytes];
        buffer.get(data);
        long tmp=0;
        for(int i=0;i<data.length;i++){
            //data[i]&0xFF 将有符号数转换成无符号数
            tmp+=((data[i]&0xFF)<<(8*(data.length-i-1)));
        }
        return getObj(tmp+"");
    }

    public Object getObj(String obj){
        return obj;
    }

    public static void main(String[] args) {
        ByteBuffer byteBuffer =ByteBuffer.allocate(1024);
        byteBuffer.putInt(255);
        byteBuffer.flip();
        Object parse = AllType.INT32.parse(byteBuffer);
        System.out.println(parse);
    }
}
