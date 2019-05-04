package com.februy.chat_server.servermanager;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class ListenerThread implements Runnable {
	
	private Selector selector;
	
	private ServerSocketChannel serverSocketChannel;
	
	private ExecutorService readPool;
	
	private AtomicInteger onlineUsers;
	
	public ListenerThread(Selector selector,ServerSocketChannel serverSocketChannel,ExecutorService readPool,AtomicInteger onlineUsers) {
		this.selector=selector;
		this.serverSocketChannel=serverSocketChannel;
		this.readPool=readPool;
		this.onlineUsers=onlineUsers;
	}

	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			Iterator<SelectionKey> keysiterator=selector.selectedKeys().iterator();  
			while (keysiterator.hasNext()) {  
				SelectionKey key=keysiterator.next();   // 遍历selectionkey
				keysiterator.remove();
				if(key.isAcceptable()) {                 
					try {
						handleAcceptRequest();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(key.isReadable()) {                  // 等待读取的key
					key.interestOps(key.interestOps()&(~SelectionKey.OP_READ));
					readPool.execute(new ReadEventHandler(key,selector,onlineUsers));
				}
			}
		}
	}
	public void handleAcceptRequest() throws IOException {
		// 服务器连接客户端
		SocketChannel socketChannel=serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);
		
	}
	public void interrupt() {
		try {
			selector.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		Thread.currentThread().interrupt();
	}

}
