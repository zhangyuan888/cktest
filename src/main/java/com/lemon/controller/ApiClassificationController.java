package com.lemon.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lemon.common.ApiClassificationVO;
import com.lemon.common.Result;
import com.lemon.pojo.ApiClassification;
import com.lemon.pojo.Suite;
import com.lemon.service.ApiClassificationService;
import com.lemon.service.SuiteService;

/**
 * <p>
 * InnoDB free: 3072 kB 前端控制器
 * </p>
 *
 * @author kk    api分类信息
 * @since 2020-02-17
 */
@RestController
@RequestMapping("/apiClassification")
public class ApiClassificationController {
	
	@Autowired     //注入业务层对象
	ApiClassificationService apiClassificationService;
	@Autowired
	SuiteService suiteService;
	@GetMapping("/toIndex")    //映射路径
	public Result getWithApi(Integer projectId,Integer tab){
		Result result=null;
		if (tab==1) {
			//接口列表
			List<ApiClassificationVO> list=apiClassificationService.getWithApi(projectId);
			result=new Result("1", list, "查询分类同时也延迟加载api"); //把list集合放在result里
		}else{
			//tab=2测试集合
			List<Suite> list2=suiteService.findSuiteAndReleadtedCasesBy(projectId);
			result=new Result("1", list2);
			
		}
		return result;
	}
	//根据projectid单表查询分类信息
	@GetMapping("/findAll")
	public Result findAll(Integer projectId){
		QueryWrapper queryWrapper=new QueryWrapper();
		queryWrapper.eq("project_id", projectId);
		List<ApiClassification> list=apiClassificationService.list(queryWrapper);
		return new Result("1", list);
	}
	//如果传过来的是json的用这个
	@PostMapping("/add2")
	public Result add2(@RequestBody String jsonStr){  //@RequestBody前端的json对象转换成后端的java对象
		System.out.println(jsonStr);
		String value=jsonStr.substring(jsonStr.indexOf("["+2), jsonStr.indexOf("]")-1);
		System.out.println(value);
		//将jsonStr字符串转换成java对象
		ApiClassification apiClassification=JSON.parseObject(value, ApiClassification.class);
		//System.out.println(apiClassification);
		apiClassificationService.save(apiClassification);
		return new Result("1", "新增分类成功");
	}

}
