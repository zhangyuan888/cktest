package com.lemon.mapper;

import com.lemon.common.ApiVo;
import com.lemon.common.ApilistVO;
import com.lemon.pojo.Api;

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
public interface ApiMapper extends BaseMapper<Api> {
	//查出对应的api
	@Select("SELECT *FROM api WHERE api_classification_id=#{apiClassificationId}")
	public List<Api> findApi(Integer apiClassificationId);
	
	//对应工程下的api
	@Select("SELECT t1.*,t2.name classificationName FROM api t1,api_classification t2 WHERE t2.project_id=#{projectId} and t1.api_classification_id=t2.id;")
	public List<ApilistVO> showApiListByProject(Integer projectId);
	
	//查询分类下的api
	@Select("SELECT t1.*,t2.name classificationName FROM api t1,api_classification t2 WHERE t2.id=#{apiClassificationId} and t1.api_classification_id=t2.id;")
	public List<ApilistVO> showApiListByApiClassification(Integer apiClassificationId);
	
	//接口详情分两部分展示，一个是基本信息（两表链接操作api和user两表链接），一个是请求参数（单表操作参数表，根据apiid这个外键查询，延时加载（有many的都是延时加载），封装到list集合里，一个api对应又多个请求参数）
	//这里给username取了别名是因为根据别名这个字段去数据库查询的;查询sql显示api详情
	@Select("SELECT t1.*,t2.username createUsername FROM api t1,`user` t2 WHERE t1.create_user=t2.id and t1.id=#{apiId}")
	@Results({
		@Result(property="id",column="id"),//属性-列一一对应，列的值从查询*获取
		@Result(property="requestParams",column="id",          //根据分类的id值去第2张表里查询，这是第2张表结果    把查到的id值赋值给属性apis
		many=@Many(select="com.lemon.mapper.ApiRequestParamMapper.findAll"))//many一对多    这是第二张表查询的路径
	})
	public ApiVo findApiViewVo(Integer apiId);  //只有一个对象，所以不用集合
}
