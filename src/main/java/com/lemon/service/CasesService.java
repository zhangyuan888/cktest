package com.lemon.service;

import com.lemon.common.ApiVo;
import com.lemon.common.CaseEditVO;
import com.lemon.common.CaseListVO;
import com.lemon.pojo.Cases;

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
public interface CasesService extends IService<Cases> {
	

	public void add(Cases caseVo, ApiVo apiRunVO);

	public List<CaseListVO> showCaseUnderProject(Integer projectId);

	public List<CaseListVO> findCaseUnderSuite(String suiteId);

	public CaseEditVO findCaseEditVO(String caseId);

	public void updateCase(CaseEditVO caseEditVO);

}
