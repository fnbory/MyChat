package com.februy.chat_common.domain;

import com.februy.chat_common.enumeration.ResponseCode;
import com.februy.chat_common.enumeration.ResponseType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseHeader {
	private String sender;
	private ResponseType responsetype;
	private Integer responseCode;
	private long timestmp;
	
}
