package com.lemon.handler;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.mysql.fabric.xmlrpc.base.Data;

@Component   //加入这个注解把MyMetaObjectHandler这个对象new出来，才能调下面这个方法
public class MyMetaObjectHandler implements MetaObjectHandler{
	
	//修正一下mp自动填充的问题：@Component 每个表都是一样的字段名是没有问题的，不影响
	//插入时自动填充注册时间
	public void insertFill(MetaObject metaObject) {
		this.setFieldValByName("regtime", new Data(), metaObject);//第一个参数是数据库列名，第二个是当前系统时间，第三个不用动
		
	}
	//更新
	public void updateFill(MetaObject metaObject) {
		// TODO Auto-generated method stub
		
	}

}
