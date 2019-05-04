package com.februy.chat_common.domain;

import java.nio.channels.SocketChannel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	private String username;
	private String password;
	private SocketChannel channel;
}
