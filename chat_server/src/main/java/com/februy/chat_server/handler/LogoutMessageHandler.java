package com.februy.chat_server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import com.februy.chat_common.domain.Message;
import com.februy.chat_common.domain.Response;
import com.februy.chat_common.domain.ResponseHeader;
import com.februy.chat_common.enumeration.ResponseCode;
import com.februy.chat_common.enumeration.ResponseType;
import com.februy.chat_common.utils.ProtoStuffUtil;
import com.februy.chat_server.property.MsgProperty;
import com.februy.chat_server.userManager.UserManager;

public class LogoutMessageHandler extends MessageHandler {
	private UserManager userManager;
	@Override
	public void handle(Message message, Selector server, SelectionKey selectionKey, AtomicInteger onlineUsers)
			throws IOException {
		SocketChannel socketChannel=(SocketChannel) selectionKey.channel();
		userManager.logout(socketChannel);
		byte[] response = ProtoStuffUtil.serialize(

                new Response(ResponseHeader.builder()

                        .responsetype(ResponseType.PROMPT)

                        .responseCode(ResponseCode.LOGOUT_SUCCESS.getCode())

                        .sender(message.getMessageHeader().getSender())

                        .timestmp(message.getMessageHeader().getTimestamp()).build(),

                        MsgProperty.LOGOUT_SUCCESS.getBytes(MsgProperty.charset)));

        socketChannel.write(ByteBuffer.wrap(response));

        onlineUsers.decrementAndGet();
		
        byte[] logoutBroadcast = ProtoStuffUtil.serialize(

                new Response(

                        ResponseHeader.builder()

                                .responsetype(ResponseType.NORMAL)

                                .sender(SYSTEM_SENDER)

                                .timestmp(message.getMessageHeader().getTimestamp()).build(),

                        String.format(MsgProperty.LOGOUT_BROADCAST, message.getMessageHeader().getSender()).getBytes(MsgProperty.charset)));

        super.broadcast(logoutBroadcast, server);
	
        
        selectionKey.cancel();
        socketChannel.close();
        socketChannel.socket().close();
	}
	
}
