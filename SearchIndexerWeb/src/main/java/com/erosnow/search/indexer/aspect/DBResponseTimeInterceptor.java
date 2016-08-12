package com.erosnow.search.indexer.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.erosnow.search.common.log.IndexerLogger;
import com.erosnow.search.common.metrics.CustomMetricSet;
import com.erosnow.search.common.util.CommonUtil;

@Aspect
@Component
public class DBResponseTimeInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(DBResponseTimeInterceptor.class);

	@Autowired
	private CustomMetricSet customMetricSet;

	@Pointcut("execution(* com.erosnow.search.indexer.services.dataImport.dao..**(..))")
	public void executeDBQueryDaoMethods() {

	}

	@Around("executeDBQueryDaoMethods()")
	public Object timer(ProceedingJoinPoint pjp) throws Throwable {
		String methodName = pjp.getSignature().getName();
		Object returnObj = null;
		try {
			IndexerLogger.logDBRead(Arrays.toString(pjp.getArgs()),
					pjp.getTarget().getClass().getSimpleName() + "-" + methodName);
			long start = CommonUtil.getCurrentTimeMillis();
			returnObj = pjp.proceed();
			customMetricSet.measureApiResponseTime(methodName, CommonUtil.getCurrentTimeMillis() - start);
		} catch (Exception e) {
			LOG.error("Exception while intercepting ", e);
		}
		return returnObj;
	}
}
