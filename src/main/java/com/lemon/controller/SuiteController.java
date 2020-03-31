package com.lemon.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lemon.common.Result;
import com.lemon.pojo.ApiClassification;
import com.lemon.pojo.Suite;
import com.lemon.service.SuiteService;

/**
 * <p>
 * InnoDB free: 3072 kB 前端控制器
 * </p>
 *
 * @author kk
 * @since 2020-02-17
 */
@RestController
@RequestMapping("/suite")
public class SuiteController {
	
	@Autowired
	SuiteService SuiteService;
	
	//套件
	//1.根据projectId获得suite
	//2.添加到cases
	//3.批量添加到case_param_value    2和3是同一事务，要写到一起，一个防火阀完成这两个操作
	
	//1.根据projectId获得suite
		@GetMapping("/listAll")
		public Result findAll(Integer projectId){
			QueryWrapper queryWrapper=new QueryWrapper();
			queryWrapper.eq("project_id", projectId);
			List<Suite> list=SuiteService.list(queryWrapper);
			return new Result("1", list);
		}

}
