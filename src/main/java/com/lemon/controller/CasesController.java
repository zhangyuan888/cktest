package com.lemon.controller;


import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.lemon.common.ApiVo;
import com.lemon.common.CaseEditVO;
import com.lemon.common.CaseListVO;
import com.lemon.common.Result;
import com.lemon.pojo.ApiRequestParam;
import com.lemon.pojo.CaseParamValue;
import com.lemon.pojo.Cases;
import com.lemon.service.CaseParamValueService;
import com.lemon.service.CasesService;

/**
 * <p>
 * InnoDB free: 3072 kB 前端控制器
 * </p>
 *
 * @author kk
 * @since 2020-02-17
 */
@RestController
@RequestMapping("/cases")
public class CasesController {
	
	@Autowired
	CasesService casesService;
	
	@PostMapping("/add")
	public Result add(Cases caseVo,ApiVo apiRunVO){
	
		casesService.add(caseVo, apiRunVO);
		return new Result("1", "添加测试集成功");
	}
	@GetMapping("/showCaseUnderProject")
	public Result showCaseUnderProject(Integer projectId){	
		List<CaseListVO> list= casesService.showCaseUnderProject(projectId);	
		return new Result("1", list);
	}
	
	@GetMapping("/findCaseUnderSuite")
	public Result findCaseUnderSuite(String suiteId){	
		List<CaseListVO> list= casesService.findCaseUnderSuite(suiteId);	
		return new Result("1", list);
	}
	//得到页面的渲染数据，套件-用例编辑
	@GetMapping("/toCaseEdit")
	public Result toCaseEdit (String caseId){	
		CaseEditVO caseEditVO= casesService.findCaseEditVO(caseId);	
		return new Result("1", caseEditVO);
	}
	
	//更新用例
	@PutMapping("/update")
	public Result updateCase(CaseEditVO caseEditVO){   //传的数据是我们的封装对象
		
		casesService.updateCase(caseEditVO);
		return new Result("1", "更新用例成功");
	}
}
