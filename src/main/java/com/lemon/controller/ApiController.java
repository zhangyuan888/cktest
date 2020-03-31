package com.lemon.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.Insert;
import com.lemon.common.ApiRunResult;
import com.lemon.common.ApiVo;
import com.lemon.common.ApilistVO;
import com.lemon.common.Result;
import com.lemon.service.ApiRequestParamService;
import com.lemon.service.ApiService;

/**
 * <p>
 * InnoDB free: 3072 kB 前端控制器
 * </p>
 *
 * @author kk
 * @since 2020-02-17
 */
@RestController
@RequestMapping("/api")
public class ApiController {
	
	@Autowired 
	ApiService apiService;
	
	@Autowired
	ApiRequestParamService apiRequestParamService;
	
	//获取全部接口
	@GetMapping("/showApiUnderProject")
	public Result showApiListByProject(Integer projectId){
		
		List<ApilistVO> list=apiService.showApiListByProject(projectId);
		Result result=new Result("1", list);
		return result;
	}
	//获取点击左侧菜单，显示对应 的接口
	@GetMapping("/showApiUnderApiClassification")
	public Result showApiUnderApiClassification(Integer apiClassificationId){
		List<ApilistVO> list=apiService.showApiListByApiClassification(apiClassificationId);
		Result result=new Result("1", list);
		return result;	
	}

	//获取点击右侧子菜单，显示对应 的接口详情
	@GetMapping("/toApiView")
	public Result findApiViewVo(Integer apiId){
		ApiVo api=apiService.findApiViewVo(apiId);
		Result result=new Result("1", api);
		return result;	
	}
	//编辑api接口  更新操作
	@PutMapping("/edit")
	public Result toApiEdit(ApiVo apiEdit){  //带过来的参数放进ApiVo
		//1.直接根据apiId进行更新api   更新操作可能是新增也可能是修改，所以为了统一，先把原来的删了，在进行插入操作
		apiService.updateById(apiEdit);
		//2.delete原来的apiRequestParam
		QueryWrapper queryWrapper=new QueryWrapper();
		queryWrapper.eq("api_id", apiEdit.getId());		
		apiRequestParamService.remove(queryWrapper);//根据外键apiid进行删除
		//3.insert apiRequestParam	 把整个子集的元素都追加到list集合，所以用addAll
		apiEdit.getRequestParams().addAll(apiEdit.getQueryParams());//前端把值给子集，子集再把值给总的 ，由总的去操作数据库
		apiEdit.getRequestParams().addAll(apiEdit.getHeaderParams());
		apiEdit.getRequestParams().addAll(apiEdit.getBodyParams());
		apiEdit.getRequestParams().addAll(apiEdit.getBodyRawParams());
		
		apiRequestParamService.saveBatch(apiEdit.getRequestParams());//saveBatch是批量添加
	
		Result result=new Result("1", "更新成功");
		return result;		
	}
	@RequestMapping("/run")
	public Result run(ApiVo apiRunVo){
		ApiRunResult apiRunResult=apiService.run(apiRunVo);
		Result result=new Result("1", apiRunResult);
		return result;
	}
}
