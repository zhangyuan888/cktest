package com.lemon.service.impl;

import com.lemon.pojo.ApiRequestParam;
import com.lemon.pojo.CaseParamValue;
import com.lemon.pojo.Cases;
import com.lemon.common.ApiVo;
import com.lemon.common.CaseEditVO;
import com.lemon.common.CaseListVO;
import com.lemon.common.Result;
import com.lemon.mapper.CasesMapper;
import com.lemon.service.CaseParamValueService;
import com.lemon.service.CasesService;
import com.lemon.service.TestRuleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * InnoDB free: 3072 kB 服务实现类
 * </p>
 *
 * @author kk
 * @since 2020-02-17
 */
@Service
public class CasesServiceImpl extends ServiceImpl<CasesMapper, Cases> implements CasesService {

	@Autowired
	CaseParamValueService caseParamValueService;
	
	@Autowired
	TestRuleService testRuleService;
	
	@Autowired
	CasesMapper casesMapper;
	public void add(Cases caseVo,ApiVo apiRunVO){
		//2.添加到cases	     同一事物放到业务层写，标准，这个方法默认会用到@EnableTransactionManagement 同一事物这个注解
		this.save(caseVo);
		//3.批量添加到case_param_value
		List<ApiRequestParam> requestParams=apiRunVO.getRequestParams();
	
		List<CaseParamValue> caseParamValues=new ArrayList<CaseParamValue>();
		for (ApiRequestParam apiRequestParam : requestParams) {
			CaseParamValue caseParamValue=new CaseParamValue();
			caseParamValue.setCaseId(caseVo.getId());  //这个getId是数据库case_param_value里的case_id
			caseParamValue.setApiRequestParamId(apiRequestParam.getId());
			caseParamValue.setApiRequestParamValue(apiRequestParam.getValue());
			caseParamValues.add(caseParamValue);
		}
		caseParamValueService.saveBatch(caseParamValues);
		
	}
	
	public List<CaseListVO> showCaseUnderProject(Integer projectId){
		return casesMapper.showCaseUnderProject(projectId);		
	}

	public List<CaseListVO> findCaseUnderSuite(String suiteId) {
		return casesMapper.findCaseUnderSuite(suiteId);
	}
	
	public CaseEditVO findCaseEditVO(String caseId){
		return casesMapper.findCaseEditVO(caseId);
	}
	//重写更新方法
	public void updateCase(CaseEditVO caseEditVO) {
		// 此时只能更新case表和case_param_value表，还需要更新test_rule表
		//根据主键更新   case表
		updateById(caseEditVO);
		//更新case_param_value表
		List<ApiRequestParam> requestParams=caseEditVO.getRequestParams();
		
		List<CaseParamValue> list=new ArrayList<CaseParamValue>();
		for (ApiRequestParam apiRequestParam : requestParams) {
			CaseParamValue caseParamValue=new CaseParamValue();
			caseParamValue.setId(apiRequestParam.getValueId());
			caseParamValue.setApiRequestParamId(apiRequestParam.getId());
			caseParamValue.setApiRequestParamValue(apiRequestParam.getValue());
			caseParamValue.setCaseId(caseEditVO.getId());
			list.add(caseParamValue);
		}
		caseParamValueService.updateBatchById(list);//批量更新
		
		//更新test_rule表      先删除test_rule,再inster
		//这张表可能是新增也可能是更新，两种方法，1 删除再插入（这个主键有没有被被人引用，如果没有的话可以这么做）更新主键是不更新的  
		//如果主键被被人引用了，则只能     把前端传过来的list1和后端数据库里的list2做对比，如果id有值则更新，为空的话则删除，如果都没有id则做新增
		QueryWrapper queryWrapper=new QueryWrapper();
		queryWrapper.eq("case_id", caseEditVO.getId());
		testRuleService.remove(queryWrapper);  //根据caseid删除
		
		testRuleService.saveBatch(caseEditVO.getTestRules());//批量插入
	}
}
