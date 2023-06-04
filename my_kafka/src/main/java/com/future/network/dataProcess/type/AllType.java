package com.future.network.dataProcess.type;

import jdk.nashorn.internal.objects.annotations.Property;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public enum AllType {
    INT16(2,"INT16","",Integer.class),
    INT32(4,"INT16","",Integer.class),
    NULLABLE_STRING(4,"INT16","",String.class){
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
        int tmp=0;
        for(int i=0;i<data.length;i++){
            //data[i]&0xFF 将有符号数转换成无符号数
            tmp+=((data[i]&0xFF)<<(8*(data.length-i-1)));
        }
        return tmp;
    }

    public static void main(String[] args) {
        ByteBuffer byteBuffer =ByteBuffer.allocate(1024);
        byteBuffer.putInt(255);
        byteBuffer.flip();
        Object parse = AllType.INT32.parse(byteBuffer);
        System.out.println(parse);
    }
}
