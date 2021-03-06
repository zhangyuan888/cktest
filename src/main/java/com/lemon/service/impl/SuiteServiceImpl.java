package com.lemon.service.impl;

import com.lemon.pojo.Suite;
import com.lemon.mapper.SuiteMapper;
import com.lemon.service.SuiteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * InnoDB free: 3072 kB 服务实现类
 * </p>
 *
 * @author kk
 * @since 2020-02-17
 */
@Service
public class SuiteServiceImpl extends ServiceImpl<SuiteMapper, Suite> implements SuiteService {

	@Autowired
	SuiteMapper suiteMapper;
	
	public List<Suite> findSuiteAndReleadtedCasesBy(Integer projectId) {
		// TODO Auto-generated method stub
		return suiteMapper.findSuiteAndReleadtedCasesBy(projectId);
	}

}
