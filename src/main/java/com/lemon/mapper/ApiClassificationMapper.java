package com.lemon.mapper;

import com.lemon.common.ApiClassificationVO;
import com.lemon.pojo.ApiClassification;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * InnoDB free: 3072 kB Mapper 接口
 * </p>
 *
 * @author kk
 * @since 2020-02-17
 */
public interface ApiClassificationMapper extends BaseMapper<ApiClassification> {
	//两表延时加载，先查询分类信息，在分类信息里有关联一个list集合，按需加载（即此时再去查询另一张表）
	//获取分类信息和api信息      先查询第一张表，把结果封装到result里
	@Select("SELECT * FROM api_classification WHERE project_id=#{projectId}")//#{projectId}相当于一个占位符？问号
	@Results({
		@Result(column="id",property="id"),//属性-列一一对应，列的值从查询*获取
		@Result(column="project_id",property="projectId"),
		@Result(column="name",property="name"),
		@Result(property="apis",column="id",          //根据分类的id值去第2张表里查询，这是第2张表结果    把查到的id值赋值给属性apis
		many=@Many(select="com.lemon.mapper.ApiMapper.findApi"))//many一对多    这是第二张表查询的路径
	})
	//获取分类信息和api信息
	public List<ApiClassificationVO> getWithApi(Integer projectId);

}
