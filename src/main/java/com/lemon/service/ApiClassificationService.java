package com.lemon.service;

import com.lemon.common.ApiClassificationVO;
import com.lemon.pojo.ApiClassification;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * InnoDB free: 3072 kB 服务类
 * </p>
 *
 * @author kk
 * @since 2020-02-17
 */
public interface ApiClassificationService extends IService<ApiClassification> {

	public List<ApiClassificationVO> getWithApi(Integer projectId);

}
