package com.ztj.jwtdemo.common.util;

import com.alibaba.fastjson.JSON;
import com.ztj.jwtdemo.common.constant.SchedulerConstant;
import com.ztj.jwtdemo.model.ScheduleJob;
import com.ztj.jwtdemo.model.ScheduleJobLog;
import com.ztj.jwtdemo.service.IScheduleJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.*;


/**
 * 定时任务
 * 
 */
@Component
@Slf4j
public class ScheduleJobUtil extends QuartzJobBean {

	@Autowired
	private IScheduleJobLogService scheduleJobLogService;

	private ExecutorService service = new ThreadPoolExecutor(5, 5, 0L,
			TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		String jsonJob = context.getMergedJobDataMap().getString(ScheduleJob.JOB_PARAM_KEY);
		ScheduleJob scheduleJob = JSON.parseObject(jsonJob, ScheduleJob.class);


        //数据库保存执行记录
        ScheduleJobLog scheduleJobLog = new ScheduleJobLog();
		scheduleJobLog.setJobId(scheduleJob.getId());
		scheduleJobLog.setBeanName(scheduleJob.getBeanName());
		scheduleJobLog.setMethodName(scheduleJob.getMethodName());
		scheduleJobLog.setParams(scheduleJob.getParams());
		scheduleJobLog.setCrtTime(new Date());
        
        //任务开始时间
        long startTime = System.currentTimeMillis();
        
        try {
            //执行任务
        	log.info("任务准备执行，任务ID：" + scheduleJob.getId());
            ScheduleRunnableUtil task = new ScheduleRunnableUtil(scheduleJob.getBeanName(),
            		scheduleJob.getMethodName(), scheduleJob.getParams());

			Future<?> future = service.submit(task);
			future.get();

			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			scheduleJobLog.setTimes((int)times);
			scheduleJobLog.setStatus(SchedulerConstant.EXECUTE_SUCCESS);
			log.info("任务执行完毕，任务ID：" + scheduleJob.getId() + "  总共耗时：" + times + "毫秒");

		} catch (Exception e) {
			log.error("任务执行失败，任务ID：" + scheduleJob.getId(), e);
			
			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			scheduleJobLog.setTimes((int)times);
			scheduleJobLog.setStatus(SchedulerConstant.EXECUTE_FAILED);
			scheduleJobLog.setError(StringUtils.substring(e.toString(), 0, 2000));
		} finally {
			scheduleJobLogService.insertSelective(scheduleJobLog);
		}
    }
}
