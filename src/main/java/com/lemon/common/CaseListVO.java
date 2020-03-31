package com.lemon.common;

import com.lemon.pojo.TestReport;

import lombok.Data;

@Data
public class CaseListVO {
	//测试集合
	private String id;  //用例id，名字，
	private String name;
	private String apiId;
	private String apiUrl;
	
	private TestReport testReport;  //一个测试用例对应一个头测试报告

}
