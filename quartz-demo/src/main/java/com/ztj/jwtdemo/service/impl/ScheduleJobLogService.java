package com.ztj.jwtdemo.service.impl;

import com.ztj.jwtdemo.dao.ScheduleJobLogMapper;
import com.ztj.jwtdemo.model.ScheduleJobLog;
import com.ztj.jwtdemo.service.IScheduleJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScheduleJobLogService implements IScheduleJobLogService{

    @Autowired
    private ScheduleJobLogMapper scheduleJobLogMapper;

    @Override
    public void insertSelective(ScheduleJobLog log) {
        scheduleJobLogMapper.insertSelective(log);
    }
}
