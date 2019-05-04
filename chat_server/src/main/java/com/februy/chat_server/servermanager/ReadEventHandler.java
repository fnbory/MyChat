package com.februy.chat_server.servermanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import com.februy.chat_common.domain.Message;
import com.februy.chat_common.utils.ProtoStuffUtil;
import com.februy.chat_server.handler.MessageHandler;

public class ReadEventHandler implements Runnable{
	 public static final int DEFAULT_BUFFER_SIZE = 1024;
	 private ByteBuffer buf;
     private SocketChannel client;
     private ByteArrayOutputStream baos;
     private SelectionKey key;
     private Selector selector;
     private AtomicInteger onlineUsers;
     
     public ReadEventHandler(SelectionKey key,Selector selector,AtomicInteger onlineUsers) {
         this.client = (SocketChannel) key.channel();
         this.buf = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
         this.baos = new ByteArrayOutputStream();
         this.key = key;
         this.selector=selector;
         this.onlineUsers=onlineUsers;
     }
	@Override
	public void run() {
		int size;
		try {
			while((size=client.read(buf))>0) {
				buf.flip();
				baos.write(buf.array(), 0, size);
				buf.clear();
			}
			if(size==-1)  return; // 此时读取完毕
			key.interestOps(key.interestOps() | SelectionKey.OP_READ);
			key.selector().wakeup();
			byte[] bytes=baos.toByteArray();
			baos.close();
			Message message=ProtoStuffUtil.deserialize(bytes, Message.class);
			MessageHandler messageHandler=null;
			messageHandler.handle(message,selector, key, onlineUsers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
