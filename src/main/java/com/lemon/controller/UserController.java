package com.lemon.controller;


import java.util.Date;



import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lemon.common.Result;
import com.lemon.pojo.User;
import com.lemon.service.UserService;
import com.mysql.fabric.xmlrpc.base.Data;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * InnoDB free: 3072 kB 前端控制器
 * </p>
 *
 * @author kk
 * @since 2020-02-17
 */
@RestController   //1.对控制层的所有方法，都会把java类转化成json，jaskon，jar包转化;2.也可以new控制层所有类，即new所有对象
@RequestMapping("/user")
@Api("用户模块")
//@CrossOrigin  //支持跨域，是浏览器到服务器，因为前后端域名不一样，一个是ck.org,一个是admin.org
				//如果是js先请求前端服务器，前端再请求后端，那么sessionId就无法追踪了，所以再前端头信息里要把sessionId带过去
				//不用这个注解了，现在做成统一跨越，对所有的控制层处理，写了一个类

public class UserController {
	@Autowired    //依赖注入到userService中；di-将对象赋值给属性
	private UserService userService;
	//注册方法    请求映射    spring ioc容器==new对象  拿到对象用di注入
	@RequestMapping("/register")  //既可支持post，也可支持get
	//@PostMapping("/register")   //注册方法    请求映射
	//@ApiOperation(value="注册方法",httpMethod= "POST")  //这个是为了显示在swagger2的文档上的
	public Result register(User user){
		//设置时间
	//	user.setRegtime(new Date());
		//调用业务层方法(本来是要创建对象的，但是用注解后就不用了)，插入到DB，统一处理异常
		userService.save(user);
		Result result=new Result("1", "注册成功");
		return result;		
	}
	
	//账号验重方法
	@GetMapping("/find")
	//@ApiOperation(value="账号验重方法",httpMethod= "GET")
	public Result find(String username){
		Result result=null;
		//调用业务层方法，查询DB非主键列，统一处理异常
		QueryWrapper queryWrapper=new QueryWrapper(); //这个类封装了查询条件
		queryWrapper.eq("username", username);
		User user =userService.getOne(queryWrapper);
		if (user==null) {
			result=new Result("1", "账号不存在");
		}else{
			result=new Result("0", "账号已存在");
		}
		return result;
	}
	
	//登录
	@PostMapping("/login")   
	@ApiOperation(value="登录方法",httpMethod= "POST")  //这个是为了显示在swagger2的文档上的
	public Result login(User user){
		Result result=null;
		//用户的账号和密码封装成一个token
		try {
			UsernamePasswordToken token=new UsernamePasswordToken(user.getUsername(),user.getPassword());
			//shiro安全验证
			Subject subject=SecurityUtils.getSubject(); //得到当前操作的对象
			subject.login(token);  //当前操作的对象要验证是否安全,这里需要传入一个token
			//因为token返回类型是void，所以需要捕捉异常，出现异常的话说明token验证不通过
			//将sessionId返回去，因为跨了前端服务器，所以需要写回去
			String sessionId=(String) SecurityUtils.getSubject().getSession().getId();
			User loginUser=(User) subject.getPrincipal();  //获取用户id，就是用户对象
			result=new Result("1",loginUser.getId(),sessionId);   //第二次请求时，前端浏览器发出异步请求时自行携带sessionId，可以放在请求头或者请求体中，我们放在请求头里好些
		} catch (AuthenticationException e) {
			if (e instanceof UnknownAccountException) {  //未知用户异常
				result=new Result("0", "用户名错误");
			}else{
				result=new Result("0", "密码错误");
			}
			e.printStackTrace();
		}  
		
		return result;		
	}
	
	//退出登录
		@GetMapping("/logout")
		@ApiOperation(value="退出方法",httpMethod= "GET")
		public Result logout(){
			Result result=null;
			//从shiro退出
			SecurityUtils.getSubject().logout();		
			result=new Result("1", "账号未登录");
			return result;			
		}
		
		@GetMapping("/unauth")
		@ApiOperation(value="未授权方法",httpMethod= "GET")
		public Result unauth(){
			Result result=null;		
			result=new Result("1", "账号未登录");
			return result;			
		}

}
