package com.februy.chat_common.enumeration;

public enum MessageType {
	LOGIN(1,"登陆"),
	LOGOUT(2,"注销"),
	NORMAL(3,"单发"),
	BROADCAST(4,"群发");
	
	private int code;
	private String desc;
	private MessageType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
