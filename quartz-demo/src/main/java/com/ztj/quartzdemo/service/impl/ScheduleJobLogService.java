package com.ztj.quartzdemo.service.impl;

import com.ztj.quartzdemo.dao.ScheduleJobLogMapper;
import com.ztj.quartzdemo.model.ScheduleJobLog;
import com.ztj.quartzdemo.service.IScheduleJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScheduleJobLogService implements IScheduleJobLogService {

    @Autowired
    private ScheduleJobLogMapper scheduleJobLogMapper;

    @Override
    public void insertSelective(ScheduleJobLog log) {
        scheduleJobLogMapper.insertSelective(log);
    }
}
