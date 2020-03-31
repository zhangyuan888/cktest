package com.lemon.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**shiro的配置
 * @author Administrator
 *
 */
@Configuration    //表示这个类是配置类   tomcat启动会自动调用配置类方法，在启动的时候那些对象也会new出来的
public class ShiroConfig {
	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置不会被拦截的链接 顺序判断
		
		filterChainDefinitionMap.put("/user/login", "anon");
		filterChainDefinitionMap.put("/user/find", "anon");
		filterChainDefinitionMap.put("/user/register", "anon");

		// 过滤链定义，从上向下顺序执行
		// authc:url都必须认证通过才可以访问; anon:url都可以匿名访问
		filterChainDefinitionMap.put("/**", "authc");
	
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		// 如果不设置默认会自动寻找Web工程根目录下的"/login"页面
		shiroFilterFactoryBean.setLoginUrl("/user/unauth");
		return shiroFilterFactoryBean;
	}

	// 重新设置SecurityManager，通过自动以的MyRealm完成登录校验：
	@Bean  //这个指的是实例好的对象放到spring容器中   获取一个bean对象
	public MyRealm myReal() {
		return new MyRealm();  //这里指回调MyRealm()这个类的方法
	}

	@Bean
	public SecurityManager securityManager(MyRealm myReal) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		 //自定义session管理，这里sessionManager是我自己自定义的，方法自己写的
        securityManager.setSessionManager(sessionManager());
		// 设置realm
		securityManager.setRealm(myReal); //把域对象告诉securityManager
		return securityManager;
	}

	@Bean
	public SessionManager sessionManager(){
		CustomSessionManager manager = new CustomSessionManager();
	    manager.setSessionDAO(new EnterpriseCacheSessionDAO());
		return manager;
	}

//会话管理被shiro拦截了，要先比对sessionId，

}
