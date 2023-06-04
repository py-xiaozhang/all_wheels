package com.future.network.simple;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class JavaSelect {
    private Selector selector;

    public JavaSelect() throws IOException {
        this.selector=Selector.open();
    }

    public void register(ServerSocketChannel socketChannel) throws IOException {
       SocketChannel channel = socketChannel.accept();
//       channel.configureBlocking(false);

       channel.configureBlocking(false) ;
       channel.socket().setTcpNoDelay(true);
       channel.socket().setKeepAlive(true);
       channel.socket().setSendBufferSize(1023);
       channel.register(selector,SelectionKey.OP_READ);
    }

    public void selectKey() throws IOException {
        selector.select(3000);
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()){
            SelectionKey selectionKey = iterator.next();
            if (selectionKey.isReadable()){
                Object attachment = selectionKey.attachment();
                System.out.println(attachment);
            }
            iterator.remove();
        }

    }

    public static void main(String[] args) {
        int num=(SelectionKey.OP_READ
                | SelectionKey.OP_WRITE
                | SelectionKey.OP_CONNECT);
        System.out.println(1&~num);
    }

}
