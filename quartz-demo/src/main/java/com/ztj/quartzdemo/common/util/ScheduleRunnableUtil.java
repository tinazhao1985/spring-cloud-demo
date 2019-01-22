package com.ztj.quartzdemo.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 执行定时任务
 * 
 */
@Slf4j
public class ScheduleRunnableUtil implements Callable<String> {
	private Object target;
	private Method method;
	private String params;
	
	public ScheduleRunnableUtil(String beanName, String methodName, String params) throws NoSuchMethodException, SecurityException {
		this.target = SpringContextUtil.getBean(beanName);
		this.params = params;
		
		if(StringUtils.isNotBlank(params)){
			this.method = target.getClass().getDeclaredMethod(methodName, String.class);
		}else{
			this.method = target.getClass().getDeclaredMethod(methodName);
		}
	}

	@Override
	public String call() throws Exception {
		try {
			ReflectionUtils.makeAccessible(method);
			if(StringUtils.isNotBlank(params)){
				method.invoke(target, params);
			}else{
				method.invoke(target);
			}
			return "";
		} catch (Exception e) {
			String msg = e.getMessage();
			if(e.getCause() instanceof RuntimeException){
				msg = e.getCause().getMessage();
			}
			log.error("run error: ", msg);
			throw new Exception(msg);
		}
	}

}
