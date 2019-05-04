package com.februy.chat_common.domain;

import com.februy.chat_common.enumeration.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageHeader {
	private String sender;
	private String receiver;
	private MessageType messageType;
	private Long timestamp;
}
