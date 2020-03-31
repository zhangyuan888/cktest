package com.lemon.controller;


import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lemon.common.Result;
import com.lemon.pojo.Project;
import com.lemon.pojo.User;
import com.lemon.service.ProjectService;
import com.lemon.service.UserService;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * InnoDB free: 3072 kB 前端控制器
 * </p>
 *
 * @author kk
 * @since 2020-02-17
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
	@Autowired    //依赖注入到projectService中；
	private ProjectService projectService;
	
	@GetMapping("/toList")
	@ApiOperation(value="查询列表方法",httpMethod= "GET")
	public Result toList(Integer userId){
		Result result=null;
		//调用业务层方法，查询DB非主键列，统一处理异常
		QueryWrapper queryWrapper=new QueryWrapper(); //这个类封装了查询条件
		queryWrapper.eq("create_user", userId);  //create_user这个是数据库的列名
		List list=projectService.list(queryWrapper);		
		result=new Result("1",list, "项目列表");
		return result;
	}
	@PostMapping("/add")
	public Result add(Project project){     //传过来普通的参数，form表单
		Result result=null;
		User user =(User) SecurityUtils.getSubject().getPrincipal();
		project.setCreateUser(user.getId());		
		projectService.save(project);  //新增项目
		result=new Result("1", "项目新增成功");
		return result;
	}
	@PostMapping("/add2")   //这个控制层方法支持json输入
	public Result add2(@RequestBody Project project){  //传过来的是json,也可以直接这样写，就不用在体里面转化了，把这些数据封装在java对应的形式参数里
		Result result=null;
		User user =(User) SecurityUtils.getSubject().getPrincipal();
		project.setCreateUser(user.getId());		
		projectService.save(project);  //新增项目
		result=new Result("1", "项目新增成功");
		return result;
	}
	//根据主键列查询项目
	@GetMapping("/{projectId}")   //@PathVariable{"projectId"}意思是把路径上的变量（符合rest风格）赋值给他同名的参数projectId；{projectId}这样写标识他是一个变量
	public Result getByID(@PathVariable("projectId") Integer projectId){
		Result result=null;
		Project project=projectService.getById(projectId);		
		result=new Result("1",project, "查询项目");
		return result;
	}
	
	@PutMapping("/{projectId}")  
	public Result updateById(@PathVariable("projectId") Integer projectId,Project project){
		Result result=null;
		project.setId(projectId);
		User user =(User) SecurityUtils.getSubject().getPrincipal();
		project.setCreateUser(user.getId());
		projectService.updateById(project);
		result=new Result("1",project, "更新项目");
		return result;
	}
}
