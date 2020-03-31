package com.lemon.mapper;

import com.lemon.common.CaseEditVO;
import com.lemon.common.CaseListVO;
import com.lemon.pojo.Cases;

import java.util.List;

import org.apache.ibatis.annotations.Case;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
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
public interface CasesMapper extends BaseMapper<Cases> {
	@Select("SELECT *FROM cases WHERE suite_id=#{suiteId}")
	List<Cases> findAll(Integer suiteId);
	
	//根据projectId查询所有cases，因为一进来没有点套件，是展示全部的，所以分两步查询，这是第一步，下面是第二步
	@Select("SELECT DISTINCT t1.*, t6.id apiId,t6.url apiUrl FROM cases t1 JOIN suite t2 ON t1.suite_id = t2.id JOIN project t3 ON t2.project_id = t3.id JOIN case_param_value t4 ON t1.id = t4.case_id JOIN api_request_param t5 ON t4.api_request_param_id = t5.id JOIN api t6 ON t5.api_id = t6.id WHERE t3.id =#{projectId}")
	@Results({
		@Result(property="id",column="id"),	//这个id是下面这个关联对象的参数	 
		@Result(property="testReport",column="id",one=@One(select="com.lemon.mapper.TestReportMapper.findByCaseId"))//column="id"外键，从上面id获取的,one是一个关联对象
		
	})
	List<CaseListVO> showCaseUnderProject(Integer projectId);
	
	//根据suites查找所有cases用例   点击左边，展示右边
	@Select("SELECT DISTINCT t1.*, t6.id apiId,t6.url apiUrl FROM cases t1 JOIN suite t2 ON t1.suite_id = t2.id JOIN case_param_value t4 ON t1.id = t4.case_id JOIN api_request_param t5 ON t4.api_request_param_id = t5.id JOIN api t6 ON t5.api_id = t6.id WHERE t1.suite_id = #{suiteId}")
	@Results({
		@Result(property="id",column="id"),	//这个id是下面这个关联对象的参数	 
		@Result(property="testReport",column="id",one=@One(select="com.lemon.mapper.TestReportMapper.findByCaseId"))//column="id"外键，从上面id获取的,one是一个关联对象
		
	})
	List<CaseListVO> findCaseUnderSuite(String suiteId);//@Results里的查询是为了查询套件一进来的页面就显示测试用例通过和不通过的图标状态，根据测试报告返回的结果去查询
	//根据caseId查询CaseEditVO中我们需要的case数据和api数据，参数信息在下一张表查
	@Select("SELECT DISTINCT t1.*, t6.id apiId,t6.method,t6.url FROM cases t1 JOIN case_param_value t2 ON t2.case_id = t1.id JOIN api_request_param t3 ON t2.api_request_param_id = t3.id JOIN api t6 ON t3.api_id = t6.id WHERE t1.id = #{caseId}")
	@Results({
		@Result(property="id",column="id"),//属性-列一一对应，列的值从查询*获取
		 //根据分类的id值去第2张表里查询，这是第2张表结果    把查到的id值赋值给属性apis
		@Result(property="requestParams",column="id",many=@Many(select="com.lemon.mapper.ApiRequestParamMapper.findByCase"))//many一对多    这是第二张表查询的路径，把查询的结果封装到requestParams里
		,@Result(property="testRules",column="id",many=@Many(select="com.lemon.mapper.TestRuleMapper.findByCase"))
	})
	CaseEditVO findCaseEditVO(String caseId);//通过caseid获取case整个详情封装到CaseEditVO对象里
	
	
	
	

}
