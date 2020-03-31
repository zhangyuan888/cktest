package com.lemon.common;

import java.util.ArrayList;
import java.util.List;

import com.lemon.pojo.ApiRequestParam;
import com.lemon.pojo.Cases;
import com.lemon.pojo.TestRule;

import lombok.Data;

//存放用例和api数据以及参数    查询结果封装为这个对象
@Data
public class CaseEditVO extends Cases{
//	private Integer id;    //id和name是用例的数据
//	private String name;
	private String method;  //这三个是接口的数据
	private String url;
	private Integer apiId;
	private String host;
	//用例的参数和参数值
	private List<ApiRequestParam> requestParams=new ArrayList<ApiRequestParam>();
	
	private List<TestRule> testRules=new ArrayList<TestRule>();//一个case有多个规则，所以用list集合
	
}
