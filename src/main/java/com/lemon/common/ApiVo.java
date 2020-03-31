package com.lemon.common;

import java.util.ArrayList;
import java.util.List;

import com.lemon.pojo.Api;
import com.lemon.pojo.ApiRequestParam;

import lombok.Data;
//api详情实体类
@Data
public class ApiVo extends Api{
	//创建人   在api详情里显示的
	private String createUsername;
	private String host;
	
	//请求参数放在list集合，最后4个对应的参数存放类型  requestParams是后端操作的请求，总的请求参数
	private List<ApiRequestParam> requestParams=new ArrayList<ApiRequestParam>();
	//最后4个用来封装前端发送过来的请求的name，前端name变了，后端也要变 ； 前端把总的请求分成了下面4个
	private List<ApiRequestParam> queryParams=new ArrayList<ApiRequestParam>();
	//2==body请求参数，form键值对，application /xx--xxx，post请求
	private List<ApiRequestParam> bodyParams=new ArrayList<ApiRequestParam>();
	private List<ApiRequestParam> headerParams=new ArrayList<ApiRequestParam>();
	//body请求参数，类型与2不一致，application/json格式xx：xx
	private List<ApiRequestParam> bodyRawParams=new ArrayList<ApiRequestParam>();
}

