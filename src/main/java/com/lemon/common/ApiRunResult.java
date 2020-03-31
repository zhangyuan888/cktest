package com.lemon.common;

import org.springframework.http.HttpHeaders;

import lombok.Data;

//封装运行结果
@Data
public class ApiRunResult {
	private String statusCode;  //响应状态码
	private String headers; //响应头     HttpHeaders底层 是MultiValueMap，是map集合， 需要转string  
	private String body;  //响应体

}
