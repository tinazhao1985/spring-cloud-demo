package com.ztj.jwtdemo.service;

import com.ztj.jwtdemo.model.ScheduleJobLog;

public interface IScheduleJobLogService {

    void insertSelective(ScheduleJobLog log);

}
