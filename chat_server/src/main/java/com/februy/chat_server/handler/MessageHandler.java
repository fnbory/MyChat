package com.februy.chat_server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import com.februy.chat_common.domain.Message;

public abstract class MessageHandler {
	public static final String SYSTEM_SENDER = "系统提示";
	public void  broadcast(byte[] data,Selector server) throws IOException {
		for(SelectionKey selectionKey:server.keys()) {
			SocketChannel channel=(SocketChannel) selectionKey.channel();
			if(channel.isConnected()) {
				channel.write(ByteBuffer.wrap(data));
			}
		}
	}
	
	

	public abstract void handle(Message message, Selector server, SelectionKey selectionKey, AtomicInteger onlineUsers)
			throws IOException;
}
