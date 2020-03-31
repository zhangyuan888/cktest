package com.lemon.common;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lemon.pojo.TestReport;

import lombok.Data;


@Data
public class ReportVO {
	private Integer id;//套件id
	private String name;//套件名     这两个信息做第一次单表查询
	
	private String username;//测试人       在shiro里面，可以拿出来
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",timezone="GMT+8")  //将Date转string
	private Date createReportTime;//生成时间     这两个信息不需要去数据库查询，当前时间可以设置进来	
	
	private int totalCase;//总用例数，计算通过率   根据caseList查询结果统计
	private int successes;//成功通过数
	private int failures;//失败数      
	
	private List<CaseListVO> caseList;//用例信息和小的测试结果信息   caselistvo，有现成的map可以查询出来

	//获取总用例数      前端渲染数据时可以直接调用这些方法拿到数据
	public int getTotalCase(){
		return caseList.size();
	}
	//获取成功用例数
	public int getSuccesses(){
		int count1=0,count2=0;
		for (CaseListVO caseListVO : caseList) {
			TestReport report=caseListVO.getTestReport();//获取每一个测试报告
			if (report!=null) {    //如果为空的话说明没有执行过
				if (report.getPassFlag().equals("通过")) {
					count1++;
				}else{
					count2++;
				}
			}
		}
		this.successes=count1;
		this.failures=count2;
		return successes;		
	}
	
	//获取失败用例数
	public int getFailures(){
		return failures;		
	}

}
