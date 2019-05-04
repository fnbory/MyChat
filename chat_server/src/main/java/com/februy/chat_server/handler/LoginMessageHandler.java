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
import com.februy.chat_common.enumeration.ResponseCode;
import com.februy.chat_common.enumeration.ResponseType;
import com.februy.chat_common.utils.ProtoStuffUtil;
import com.februy.chat_server.property.MsgProperty;
import com.februy.chat_server.userManager.UserManager;

public class LoginMessageHandler extends MessageHandler{
	
	private UserManager userManager;
	@Override
	public void handle(Message message, Selector server, SelectionKey selectionKey,AtomicInteger onlineUsers) throws IOException {
		SocketChannel channel=(SocketChannel) selectionKey.channel();
		MessageHeader header=message.getMessageHeader();
		String username=header.getSender();
		String password=null;
		if(userManager.login(channel, username, password)) {
			byte[] response=ProtoStuffUtil.serialize(new Response(ResponseHeader.builder().responsetype(ResponseType.PROMPT).
					sender(header.getSender()).timestmp(header.getTimestamp()).responseCode(ResponseCode.LOGIN_SUCCESS.getCode()).build(),String.format(MsgProperty.LOGIN_SUCCESS,
					onlineUsers.incrementAndGet()).getBytes(MsgProperty.charset)));
		channel.write(ByteBuffer.wrap(response));
		
		byte[] loginBroadcast = ProtoStuffUtil.serialize(

                new Response(

                        ResponseHeader.builder()

                                .responsetype(ResponseType.NORMAL)

                                .sender(SYSTEM_SENDER)

                                .timestmp(message.getMessageHeader().getTimestamp()).build(),

                        String.format(MsgProperty.LOGIN_BROADCAST, message.getMessageHeader().getSender()).getBytes(MsgProperty.charset)));

       super.broadcast(loginBroadcast,server);

       
		}
		else {
			byte[] response = ProtoStuffUtil.serialize(

                    new Response(

                            ResponseHeader.builder()

                                    .responsetype(ResponseType.PROMPT)

                                    .responseCode(ResponseCode.LOGIN_FAILURE.getCode())

                                    .sender(header.getSender())

                                    .timestmp(header.getTimestamp()).build(),

                                    MsgProperty.LOGIN_FAILURE.getBytes(MsgProperty.charset)));

            channel.write(ByteBuffer.wrap(response));
		}
	}
	
}
