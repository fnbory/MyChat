package com.februy.chat_server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import com.februy.chat_common.domain.Message;
import com.februy.chat_common.domain.MessageHeader;
import com.februy.chat_common.domain.Response;
import com.februy.chat_common.domain.ResponseHeader;
import com.februy.chat_common.enumeration.ResponseType;
import com.februy.chat_common.utils.ProtoStuffUtil;
import com.februy.chat_server.property.MsgProperty;
import com.februy.chat_server.userManager.UserManager;

public class NorMalMessageHandler extends MessageHandler{
	
	private UserManager userManager;

	@Override
	public void handle(Message message, Selector server,SelectionKey selectionKey, AtomicInteger onlineUsers) throws IOException {
		SocketChannel clientchannel=(SocketChannel) selectionKey.channel();
		MessageHeader header=message.getMessageHeader();
		SocketChannel receiverChannel=userManager.getuserchannel(header.getReceiver());
		if(receiverChannel==null) {
			byte[] response=ProtoStuffUtil.serialize(new Response(ResponseHeader.builder().responsetype(ResponseType.PROMPT).sender(header.getSender()).
					timestmp(header.getTimestamp()).build(),MsgProperty.RECEIVER_LOGGED_OFF.getBytes(MsgProperty.charset)));
			clientchannel.write(ByteBuffer.wrap(response));
		}
		else {
			byte[] response=ProtoStuffUtil.serialize(new Response(ResponseHeader.builder().responsetype(ResponseType.NORMAL).sender(header.getSender()).
					timestmp(header.getTimestamp()).build(),message.getBody()));
			receiverChannel.write(ByteBuffer.wrap(response));
			clientchannel.write(ByteBuffer.wrap(response));
		}
	}
	
}
