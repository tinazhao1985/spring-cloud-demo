package com.ztj.quartzdemo.model;

import lombok.Data;

import java.util.Date;

/**
 * 定时执行日志
 * 
 */
@Data
public class ScheduleJobLog {

	/**
	 * 日志id
	 */
	private Integer id;
	
	/**
	 * 任务id
	 */
	private Integer jobId;
	
	/**
	 * spring bean名称
	 */
	private String beanName;
	
	/**
	 * 方法名
	 */
	private String methodName;
	
	/**
	 * 参数
	 */
	private String params;
	
	/**
	 * 任务状态    0：成功    1：失败
	 */
	private Integer status;
	
	/**
	 * 失败信息
	 */
	private String error;
	
	/**
	 * 耗时(单位：毫秒)
	 */
	private Integer times;
	
	/**
	 * 创建时间
	 */
	private Date crtTime;

}
