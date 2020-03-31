package com.lemon.mapper;

import com.lemon.common.CaseEditVO;
import com.lemon.common.ReportVO;
import com.lemon.pojo.TestReport;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
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
public interface TestReportMapper extends BaseMapper<TestReport> {
	//批量执行的sql
	@Select("SELECT DISTINCT t1.*, t6.id apiId, t6.method, t6.url url, t3. HOST FROM cases t1 JOIN suite t2 ON t1.suite_id = t2.id JOIN project t3 ON t2.project_id = t3.id JOIN case_param_value t4 ON t1.id = t4.case_id JOIN api_request_param t5 ON t4.api_request_param_id = t5.id JOIN api t6 ON t5.api_id = t6.id WHERE t2.id = #{suiteId}")
	@Results({
		@Result(property="id",column="id"),//属性-列一一对应，列的值从查询*获取，这里的id从上面获取
		 //根据第一张表的caseid值去第2张表里查询所有的参数和参数值，这是第2张表结果    把查到的结果值赋值给属性requestParams
		@Result(property="requestParams",column="id",many=@Many(select="com.lemon.mapper.ApiRequestParamMapper.findByCase"))//many一对多    这是第二张表查询的路径，把查询的结果封装到requestParams里
		//再根据caseid去查询这张表，查询所有的testRules
		,@Result(property="testRules",column="id",many=@Many(select="com.lemon.mapper.TestRuleMapper.findByCase"))
	})
	List<CaseEditVO> findCaseEditVosUnderSuite(Integer suiteId);
	
	@Delete("delete from test_report where case_id in(select case_id from suite where id=#{suiteId})")
	void deleteReport(Integer suiteId);  //删除没有返回值，所以用void

	
	@Select("select * from test_report where case_id=#{caseId}")
	TestReport findByCaseId(Integer caseId);
	
	@Select("select * from suite where id=#{suiteId}")  //查询总的测试报告里的id和name
	@Results({     //第二张表是根据第一张表的查询结果去查询总的测试报告里的caselist
		@Result(property="id",column="id"),//属性-列一一对应，列的值从查询*获取，这里的id从上面获取
		@Result(property="caseList",column="id",many=@Many(select="com.lemon.mapper.CasesMapper.findCaseUnderSuite"))
	})
	public ReportVO getReport(Integer suiteId);  //根据suiteid获取到了suite信息和case信息以及case下面的report信息
}
