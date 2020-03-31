package com.lemon.service;

import com.lemon.common.ReportVO;
import com.lemon.pojo.TestReport;

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
public interface TestReportService extends IService<TestReport> {
	//批量运行  根据suiteId，点击批量测试，批量执行套件下的用例，每一个用例都有一个TestReport
	public List<TestReport> run(Integer suiteId);

	public TestReport findByCaseId(Integer caseId);

	public ReportVO getReport(Integer suiteId);
}
