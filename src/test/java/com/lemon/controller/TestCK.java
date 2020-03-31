package com.lemon.controller;




import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSONPath;

//支持junit+mockmvc
@SpringBootTest    //启动对单元测试的支持
@RunWith(SpringRunner.class) 											 //RunWith：具体指定用哪个框架，指单元测试用的是junit，SpringRunner底层就是junit
@AutoConfigureMockMvc  															//把mockmvc实例化出来，放在spring容器里
public class TestCK {
	@Autowired     //取出mockmvc
	private MockMvc mockMvc;  
	
	String sessionId;
	
	//用户登录post sessionId
	@Before
	@Test
	public void testLogin() throws UnsupportedEncodingException, Exception{
		String resultJson=mockMvc.perform(MockMvcRequestBuilders.post("/user/login")//perform伪造一个请求
				.param("username", "123@qq.com")
				.param("password", "96e79218965eb72c92a549dd5a330112")
				)
				//响应
				.andDo(print())   //添加一个结果处理器,打印结果
				.andExpect(status().isOk())//添加执行完成后的断言  状态码
				.andReturn().getResponse().getContentAsString();//执行完成后返回相应的结果
				sessionId=(String) JSONPath.read(resultJson, "$.message");  //通过响应体，通过jsonpath表达式，获取sessionid
				
	}
	
	//用户验重get   这些mock的（伪造）测试数据对开发，前端都是有指导意义的
//	@Ignore
	@Test
	public void test() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/user/find")
				.header("Authorization", sessionId)
				.param("username",  "123@qq.com")
				)
		.andDo(print())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("账号已存在"))
		.andReturn();
	}
	//参数为json 新增project   单元测试也是只对输入输出关心，代码怎么实现的不关心
	@Test 
	public void testProjectAdd() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/project/add2")
				.header("Authorization", sessionId)
				.contentType(MediaType.APPLICATION_JSON)  //参数类型是json
				.content("{\"name\":\"ck\",\"host\":\"http://admin.ck.org\"}")
				)
		.andDo(print())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("1"))
		.andReturn();
	}
}
