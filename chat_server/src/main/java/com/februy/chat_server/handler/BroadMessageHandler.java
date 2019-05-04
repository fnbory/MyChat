package com.februy.chat_server.handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.atomic.AtomicInteger;

import com.februy.chat_common.domain.Message;
import com.februy.chat_common.domain.Response;
import com.februy.chat_common.domain.ResponseHeader;
import com.februy.chat_common.enumeration.ResponseType;
import com.februy.chat_common.utils.ProtoStuffUtil;

public class BroadMessageHandler extends MessageHandler{

	@Override
	public void handle(Message message,Selector server,SelectionKey selectionKey, AtomicInteger onlineUsers) throws IOException {
		byte[] response=ProtoStuffUtil.serialize(new Response(ResponseHeader.builder().responsetype(ResponseType.NORMAL).sender(message.getMessageHeader().getSender())
				.timestmp(message.getMessageHeader().getTimestamp()).build(),message.getBody()));
				
		super.broadcast(response, server);
	}	
}
