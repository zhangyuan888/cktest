package com.lemon.service.impl;

import com.lemon.pojo.Api;
import com.lemon.pojo.ApiRequestParam;
import com.lemon.common.ApiRunResult;
import com.lemon.common.ApiVo;
import com.lemon.common.ApilistVO;
import com.lemon.mapper.ApiMapper;
import com.lemon.service.ApiService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity.HeadersBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
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
public class ApiServiceImpl extends ServiceImpl<ApiMapper, Api> implements ApiService {
	
	@Autowired
	ApiMapper apiMapper;
	public List<ApilistVO> showApiListByProject(Integer projectId){
		return  apiMapper.showApiListByProject(projectId);
	}
	
	public List<ApilistVO> showApiListByApiClassification(Integer apiClassificationId) {
		return apiMapper.showApiListByApiClassification(apiClassificationId);
	}

	public ApiVo findApiViewVo(Integer apiId) {	
		return apiMapper.findApiViewVo(apiId);
	}

	public ApiRunResult run(ApiVo apiRunVo) {
		// 远程调用RestTemplate是springboot里的框架，springboot4以上都可以用
		RestTemplate restTemplate=new RestTemplate();  //RestTemplate只能走http协议
		String url=apiRunVo.getHost()+apiRunVo.getUrl();
		String method=apiRunVo.getMethod();
		List<ApiRequestParam> list=apiRunVo.getRequestParams();
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
		//如果不相等的话，肯定是get操作，有拼接操作    加这个判断，是未了程序更健壮，不加的话也可以
		if (!"?".equals(paramStr)) {
			paramStr=paramStr.substring(0, paramStr.lastIndexOf("&"));//最后一个是索引-1的，去掉最后一个&
		}
		//System.out.println("paramStr"+paramStr);
		HttpEntity httpEntity=null;
		ResponseEntity response=null;  //返回结果
		ApiRunResult apiRunResult=new ApiRunResult();
		try {
			if ("get".equals(method)) {
				httpEntity=new HttpEntity(headers);  //get请求不需要参数，所有不要bodyParams
			
				response=restTemplate.exchange(url+paramStr,HttpMethod.GET,httpEntity,String.class);			
			}
			else if ("post".equals(method)) {
				httpEntity=new HttpEntity(headers,bodyParams);
				response=restTemplate.exchange(url,HttpMethod.POST,httpEntity,String.class);
			}
			apiRunResult.setStatusCode(response.getStatusCodeValue()+"");//set赋值操作，封装为自己的结果对象
			 HttpHeaders headsResult=response.getHeaders();
			 //将java转换为json字符串   jackson转或者fastjson
			 //jackson
			// apiRunResult.setHeaders(new ObjectMapper().writeValueAsString(headsResult));
			 //fastjson转
			 apiRunResult.setHeaders(JSON.toJSONString(headsResult));
			 apiRunResult.setBody(response.getBody().toString());//toString转换为string
		} catch (HttpStatusCodeException e) {  //400，500等
			//注意此时有调用异常情况需要处理，往往没有body响应体，可能有可能没有body
			apiRunResult.setStatusCode(e.getRawStatusCode()+"");
			apiRunResult.setHeaders(JSON.toJSONString(e.getResponseHeaders()));
			apiRunResult.setBody(e.getResponseBodyAsString());
			e.printStackTrace();
		} 	 
		return apiRunResult;
	}
	
	
	
}
