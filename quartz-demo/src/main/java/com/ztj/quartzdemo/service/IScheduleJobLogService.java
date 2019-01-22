package com.ztj.quartzdemo.service;

import com.ztj.quartzdemo.model.ScheduleJobLog;

public interface IScheduleJobLogService {

    void insertSelective(ScheduleJobLog log);

}
