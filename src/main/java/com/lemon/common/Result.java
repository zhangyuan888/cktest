package com.lemon.common;

import lombok.Data;

@Data
public class Result {
	private String status;  // 1 0
	private Object data;
	private String message;
	public Result(String status, Object data) {
		super();
		this.status = status;
		this.data = data;
	}
	public Result(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	public Result(String status, Object data, String message) {
		super();
		this.status = status;
		this.data = data;
		this.message = message;
	}
	
	
	


}
