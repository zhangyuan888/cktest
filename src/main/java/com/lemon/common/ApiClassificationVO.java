package com.lemon.common;

import java.util.List;

import com.lemon.pojo.ApiClassification;

import io.swagger.annotations.Api;
import lombok.Data;

//两个数据封装在一个类里，既有分类信息，又有api信息  ApiClassification信息有了
@Data
public class ApiClassificationVO extends ApiClassification{
	//关联对象，分类对应的api信息
	List<Api> apis;
}
