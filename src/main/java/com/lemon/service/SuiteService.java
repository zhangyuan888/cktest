package com.lemon.service;

import com.lemon.pojo.Suite;

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
public interface SuiteService extends IService<Suite> {


	List<Suite> findSuiteAndReleadtedCasesBy(Integer projectId);

}
