package com.lemon.service.impl;

import com.lemon.pojo.ApiRequestParam;
import com.lemon.pojo.TestReport;
import com.lemon.pojo.TestRule;
import com.lemon.pojo.User;
import com.lemon.common.ApiRunResult;
import com.lemon.common.CaseEditVO;
import com.lemon.common.ReportVO;
import com.lemon.mapper.TestReportMapper;
import com.lemon.service.TestReportService;

import net.sf.jsqlparser.util.AddAliasesVisitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * InnoDB free: 3072 kB 服务实现类
 * </p>
 *
 * @author kk
 * @since 2020-02-17
 */
@Service
public class TestReportServiceImpl extends ServiceImpl<TestReportMapper, TestReport> implements TestReportService {
	
	@Autowired
	TestReportMapper testReportMapper;
	
	public List<TestReport> run(Integer suiteId){
		List<TestReport> reportList=new ArrayList<TestReport>();
		//1.先根据suiteId查询   
		List<CaseEditVO> list=testReportMapper.findCaseEditVosUnderSuite(suiteId);
		for (CaseEditVO caseEditVO : list) {
			//2.远程调用api执行
			TestReport report=runAndGetReport(caseEditVO);
			reportList.add(report);  //之前这个没写的时候，数据库没有值，接口返回没有数据，找了半天问题
		}
		//3对test_report先删除再插入
		testReportMapper.deleteReport(suiteId);
		this.saveBatch(reportList);   //批量插入自己本身
		return reportList;
	}

	private TestReport runAndGetReport(CaseEditVO caseEditVO) {
		TestReport testReport=new TestReport();
		RestTemplate restTemplate=new RestTemplate();  //RestTemplate只能走http协议
		String url=caseEditVO.getHost()+caseEditVO.getUrl();
		String method=caseEditVO.getMethod();
		List<ApiRequestParam> list=caseEditVO.getRequestParams();
		LinkedMultiValueMap<String, String> headers=new LinkedMultiValueMap<String, String>();//以键值对形式存放
		LinkedMultiValueMap<String, String> bodyParams=new LinkedMultiValueMap<String, String>();
		String paramStr="?";
		for (ApiRequestParam apiRequestParam : list) {
			if (apiRequestParam.getType()==3) {
				headers.add(apiRequestParam.getName(), apiRequestParam.getValue());
			}
			else if (apiRequestParam.getType()==1) {
				paramStr += apiRequestParam.getName()+"="+apiRequestParam.getValue()+"&";//&表示有多个参数的情况，这个拼接完后会多一个&，所以需要再判断，参数名，参数值
			}
			else{
				//body 2和4  注意此时type==1没有处理
				headers.add(apiRequestParam.getName(), apiRequestParam.getValue());
			}			
		}
		
		if (!"?".equals(paramStr)) {
			paramStr=paramStr.substring(0, paramStr.lastIndexOf("&"));//最后一个是索引-1的，去掉最后一个&
		}
		//System.out.println("paramStr"+paramStr);
		HttpEntity httpEntity=null;
		ResponseEntity response=null;  //返回结果
		
		try {
			if ("get".equals(method)) {
				httpEntity=new HttpEntity(headers);  //get请求不需要参数，所有不要bodyParams
			
				response=restTemplate.exchange(url+paramStr,HttpMethod.GET,httpEntity,String.class);			
			}
			else if ("post".equals(method)) {
				httpEntity=new HttpEntity(headers,bodyParams);
				testReport.setRequestBody(JSON.toJSONString(bodyParams));
				response=restTemplate.exchange(url,HttpMethod.POST,httpEntity,String.class);
			}
			testReport.setCaseId(caseEditVO.getId());
			testReport.setRequestUrl(url);//请求地址
			//请求头
			testReport.setRequestHeaders(JSON.toJSONString(headers));//把map的子集（headers）变成json串，才能存到字符串中，headers的属性是string
			testReport.setResponseHeaders(JSON.toJSONString(response.getHeaders())); //响应头
			testReport.setResponseBody(response.getBody().toString());   //响应体
			testReport.setPassFlag(assertByTestRule(response.getBody().toString(),caseEditVO.getTestRules()));// 验证规则
		
		
	}catch(Exception e){
		e.printStackTrace();
		}
		return testReport;
	}
	//验证规则，响应回来的数据(实际值)与TestRule验证规则比较
	String assertByTestRule(String responseBody,List<TestRule> testRules){
		boolean flag=true;
		for (TestRule testRule : testRules) {
			Object value=JSONPath.read(responseBody, testRule.getExpression());//JSONPath对json取正则；左边是实际值，就是那个json，右边是要取的expression，实际上就是验证的规则，通过验证规则取到这个值。等价于$.name然后赋值给value
			String actual=(String)value;  //实际值
			String op=testRule.getOperator();
			if (op.equals("=")) {
				if (!actual.equals(testRule.getExpected())) {  //如果实际值不等于期望值，则为不通过   字符串语正则表达式匹配
					flag=false;
				}
			}else{
				if (!actual.contains(testRule.getExpected())) {
					flag=false;
				}
			}
		}
		if (!flag) {   //如果flag不为true，则不通过
			return "不通过";
		}
		return "通过";   //其他情况下为通过
		
	}

	public TestReport findByCaseId(Integer caseId) {
		// TODO Auto-generated method stub
		return testReportMapper.findByCaseId(caseId);
	}
	//获取总的测试报告
	public ReportVO getReport(Integer suiteId) {
		ReportVO report=testReportMapper.getReport(suiteId);
		
		User user=(User) SecurityUtils.getSubject().getPrincipal();
		report.setUsername(user.getUsername());  //设置执行人
		report.setCreateReportTime(new Date());   //设置执行用例当前时间
		return report;
	}
	
}
