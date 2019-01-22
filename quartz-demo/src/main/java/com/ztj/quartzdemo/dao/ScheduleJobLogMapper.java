package com.ztj.quartzdemo.dao;

import com.ztj.quartzdemo.model.ScheduleJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 * 
 */
@Mapper
public interface ScheduleJobLogMapper {

    int insertSelective(ScheduleJobLog record);
	
}
