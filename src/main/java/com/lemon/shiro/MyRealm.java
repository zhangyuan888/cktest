package com.lemon.shiro;

import javax.validation.constraints.Null;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lemon.pojo.User;
import com.lemon.service.UserService;

//自己写一个域，去验证码用户名，密码
public class MyRealm extends AuthorizingRealm{
	@Autowired
	UserService userService;    //这个应该是创建业务层对象，然后调用里面的方法
	
	//授权（权限管理）
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	//身份认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//认证逻辑
		String username=token.getPrincipal().toString();//从token里拿到用户名
		QueryWrapper<User> queryWrapper=new QueryWrapper<User>();
		queryWrapper.eq("username",username);
		User dbUser=userService.getOne(queryWrapper);  //获取用户信息
		if (dbUser!=null) {
			//对比密码，不对的话抛出异常
			return new SimpleAuthenticationInfo(dbUser, dbUser.getPassword(), getName());//getName指当前类的名字MyRealm
		}
		return null;
	}

}
