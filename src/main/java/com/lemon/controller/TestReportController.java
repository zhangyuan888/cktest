package com.lemon.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.lemon.common.ReportVO;
import com.lemon.common.Result;
import com.lemon.pojo.TestReport;
import com.lemon.service.TestReportService;

/**
 * <p>
 * InnoDB free: 3072 kB 前端控制器
 * </p>
 *
 * @author kk
 * @since 2020-02-17
 */
@RestController
@RequestMapping("/testReport")
public class TestReportController {
	
	//批量运行用例
	@Autowired
	TestReportService testReportService;
	
	@RequestMapping("/run")
	public Result run(Integer suiteId){
		List<TestReport> reportList=testReportService.run(suiteId);
		Result result=new Result("1",reportList, "测试执行成功");
		return result;
		
	}
	
	@RequestMapping("/findCaseRunResult")
	public Result findCaseRunResult(Integer caseId){
		TestReport report=testReportService.findByCaseId(caseId); //根据caseid获取一个测试报告，所以不用集合
		Result result=new Result("1",report, "测试报告");
		return result;
		
	}
	
	@RequestMapping("/get")
	public Result get(Integer suiteId){
		ReportVO report=testReportService.getReport(suiteId); //根据caseid获取一个测试报告，所以不用集合
		Result result=new Result("1",report, "查询测试报告成功");
		return result;
		
	}
	

}
