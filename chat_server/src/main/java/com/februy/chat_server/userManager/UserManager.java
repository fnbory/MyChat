package com.februy.chat_server.userManager;

import java.nio.channels.SocketChannel;
import java.util.Map;

import com.februy.chat_common.domain.User;

public class UserManager {
	private Map<String, User> users;
	private Map<SocketChannel,String> onlineUsers;
	
	
	
	public  boolean login(SocketChannel channel,String username,String password) {
		if (!users.containsKey(username)) {
            return false;
        }
        User user = users.get(username);
        if (!user.getPassword().equals(password)) {
            return false;
        }
        if(user.getChannel()!=null) {
        	return false;    // 重复登陆
        }
        	user.setChannel(channel);
        	onlineUsers.put(channel, username);
        	return true;
        
        
	}
	
	public synchronized void logout(SocketChannel channel) {

        String username = onlineUsers.get(channel);
        users.get(username).setChannel(null);
        onlineUsers.remove(channel);

    }
	
	
	
	
	public synchronized SocketChannel getuserchannel(String username) {    // 注意sunchronized
		User user=users.get(username);
		if(user!=null) {
			SocketChannel channel=user.getChannel();
			if(onlineUsers.containsKey(channel)) {
				return channel;
			}
		}
		return null;
	}
}
