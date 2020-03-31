package com.lemon.mapper;

import com.lemon.pojo.Suite;

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
public interface SuiteMapper extends BaseMapper<Suite> {
	
	@Select("SELECT * FROM suite WHERE project_id=#{projectId}")
	@Results({
		@Result(property="id",column="id"),//属性-列一一对应，列的值从查询*获取
		@Result(property="cases",column="id",          //根据分类的id值去第2张表里查询，这是第2张表结果    把查到的id值赋值给属性apis
		many=@Many(select="com.lemon.mapper.CasesMapper.findAll"))//many一对多    这是第二张表查询的路径
	})
	List<Suite> findSuiteAndReleadtedCasesBy(Integer projectId);

}
