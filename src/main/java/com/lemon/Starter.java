package com.lemon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication //启动类，他会默认去扫他的子包下面所有的配置和类，如果不是他的子包就不会去扫；Soringboot子包
                       //有一个springboot启动类，其他的类就放在这个启动类对应的子包下面

@EnableSwagger2  //表示允许swagger类，使配置类生效
@MapperScan(basePackages="com.lemon.mapper")    // new mapper层的所有对象
@EnableTransactionManagement  //在业务层启用事务管理，因为对数据库的增删改都是破坏性操作，所以要事务管理，就是二次确认，是否删除
//启动类
public class Starter {
	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}

}
