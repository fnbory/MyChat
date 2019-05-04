package com.februy.chat_server.servermanager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatServer {
	private static final int port=8082;
	private static final String address="localhost";
	private ServerSocketChannel serverSocketChannel;
	private ExecutorService readPool;
	private Selector selector;
	private AtomicInteger onlineUsers;
	private ListenerThread listenerThread;
	public  void initServer() throws IOException {
		serverSocketChannel=ServerSocketChannel.open();
		Selector selector=Selector.open();
		serverSocketChannel.bind(new InetSocketAddress(address, port));
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		listenerThread=new ListenerThread(selector, serverSocketChannel,readPool,onlineUsers);
		onlineUsers=new AtomicInteger(0);
		readPool=new ThreadPoolExecutor(5, 10, 1000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());
		System.out.println("服务器启动...");
	}
	
	public void service() {
		new Thread(listenerThread).start();
	}
	
	public void shutdown() throws IOException {
		listenerThread.shutdown();
		readPool.shutdown();
		serverSocketChannel.close();
		
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("初始化中");
		ChatServer chatServer=new ChatServer();
		chatServer.initServer();
		chatServer.service();
		
	}
}
