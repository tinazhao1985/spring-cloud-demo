package com.ztj.jwtdemo.dao;

import com.ztj.jwtdemo.model.ScheduleJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 * 
 */
@Mapper
public interface ScheduleJobLogMapper {

    int insertSelective(ScheduleJobLog record);
	
}
