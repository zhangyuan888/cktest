package com.lemon.service;

import com.lemon.common.ApiRunResult;
import com.lemon.common.ApiVo;
import com.lemon.common.ApilistVO;
import com.lemon.pojo.Api;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * <p>
 * InnoDB free: 3072 kB 服务类
 * </p>
 *
 * @author kk
 * @since 2020-02-17
 */
public interface ApiService extends IService<Api> {

	public List<ApilistVO> showApiListByProject(Integer projectId);

	public List<ApilistVO> showApiListByApiClassification(Integer apiClassificationId);
	
	public ApiVo findApiViewVo(Integer apiId);

	public ApiRunResult run(ApiVo apiRunVo);
}
