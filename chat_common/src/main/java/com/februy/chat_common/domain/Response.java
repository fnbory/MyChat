package com.februy.chat_common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
	private ResponseHeader responseHeader;
	private byte[] body;
}
