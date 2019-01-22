package com.ztj.jwtdemo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ztj.jwtdemo.common.bean.BaseResponse;
import com.ztj.jwtdemo.common.bean.ErrorResponse;
import com.ztj.jwtdemo.common.bean.SuccessResponse;
import com.ztj.jwtdemo.common.bean.TableData;
import com.ztj.jwtdemo.common.constant.SchedulerConstant;
import com.ztj.jwtdemo.common.util.MessageUtil;
import com.ztj.jwtdemo.common.util.ScheduleUtils;
import com.ztj.jwtdemo.dao.ScheduleJobMapper;
import com.ztj.jwtdemo.model.ScheduleJob;
import com.ztj.jwtdemo.service.IScheduleJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ScheduleJobService implements IScheduleJobService {

	@Autowired
    private Scheduler scheduler;

	@Autowired
	private ScheduleJobMapper scheduleJobMapper;
	
	/**
	 * 项目启动时，初始化定时器
	 */
	@PostConstruct
	public void init(){
		log.info("init schedule job list. ");

		List<ScheduleJob> scheduleJobList = scheduleJobMapper.selectAll();
		for(ScheduleJob scheduleJob : scheduleJobList){
			try {
				CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getId());
				// 如果不存在，则创建
				if(cronTrigger == null) {
					ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
				} else {
					ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
				}
			} catch (SchedulerException e) {
				log.error("init scheduler job error! ", e);
			}
		}
	}

	/**
	 * 定时任务列表查询
	 *
	 * @param params
	 * @return
	 */
	@Override
	public BaseResponse selectByQuery(Map<String, Object> params) {
		if(null == params || params.isEmpty()) {
			TableData<ScheduleJob> data = new TableData<>();
			return new SuccessResponse<>(data);
		}

		// 分页参数
		int page = 1;
		int limit = 10;
		if(null != params.get("page")) {
			page = Integer.parseInt(params.get("page").toString());
			params.remove("page");
		}
		if(null != params.get("limit")) {
			limit = Integer.parseInt(params.get("limit").toString());
			params.remove("limit");
		}

		// 查询条件
		Page<Object> result = PageHelper.startPage(page, limit);
		List<ScheduleJob> list = scheduleJobMapper.selectByParams(params);
		TableData<ScheduleJob> data = new TableData<>(result.getTotal(), list);
		return new SuccessResponse(data);
	}

	/**
	 * 新增定时任务
	 *
	 * @param scheduleJob
	 * @return
	 */
	@Override
	public BaseResponse add(ScheduleJob scheduleJob) {
		// 输入校验
		String msg = checkInput(scheduleJob);
		if(StringUtils.isNotEmpty(msg)) {
			return new ErrorResponse(msg);
		}

		scheduleJob.setStatus(SchedulerConstant.ScheduleStatus.NORMAL.getValue());
		scheduleJob.setCrtTime(new Date());
		scheduleJobMapper.insertSelective(scheduleJob);

		try {
			ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
		} catch (SchedulerException e) {
			log.error("add scheduler job error! ", e);
			return new ErrorResponse("scheduler.add.failed", null);
		}

		return new SuccessResponse(null);
    }

	/**
	 * 修改定时任务
	 *
	 * @param id
	 * @param scheduleJob
	 * @return
	 */
	@Override
	public BaseResponse update(int id, ScheduleJob scheduleJob) {
		// 输入校验
		String msg = checkInput(scheduleJob);
		if(StringUtils.isNotEmpty(msg)) {
			return new ErrorResponse(msg);
		}

		ScheduleJob oldJob = scheduleJobMapper.selectById(id);
		if(null == oldJob) {
			return new ErrorResponse("operation.failed.data.notexists", null);
		}

		// 修改db
		scheduleJob.setId(id);
		scheduleJobMapper.updateById(scheduleJob);

		// 更新定时任务
		try {
			ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
		} catch (SchedulerException e) {
			log.error("udpate scheduler job error! ", e);
			return new ErrorResponse("scheduler.update.failed", null);
		}

		return new SuccessResponse(null);
    }

	/**
	 * 批量删除定时任务
	 *
	 * @param ids
	 */
	@Override
	public BaseResponse deleteBatch(List<Integer> ids) {
		StringBuffer msg = new StringBuffer();

		// 取出所有的定时任务
		List<ScheduleJob> jobs = scheduleJobMapper.selectJobListByIds(ids);
		if(null == jobs || jobs.isEmpty()) {
			return new SuccessResponse(null);
		}

		// 删除定时任务
		int failedNum = 0;
    	for(Integer id : ids){
			try {
				ScheduleUtils.deleteScheduleJob(scheduler, id);

			} catch (SchedulerException e) {
				log.error("delete scheduler job error! ", e);

				failedNum++;

				// 获取该定时任务的信息
				ScheduleJob job = getJobByIdInList(jobs, id);
				if(null == job) {
					continue;
				}

				// 拼接错误信息
				String[] name = {job.getBeanName() + SchedulerConstant.SYMBOL_DOT + job.getMethodName()};
				msg.append(MessageUtil.getMessage("scheduler.delete.failed", name));

				// 去掉删除失败的定时任务
				ids.remove(id);
			}
		}

		if(failedNum > 0) {
			if(failedNum == jobs.size()) {
				// 全部失败
				log.error("delete job all failed: {}", msg.toString());

				msg.setLength(0);
				msg.append(MessageUtil.getMessage("scheduler.delete.all.failed", null));
			} else {
				// 部分失败
				msg.insert(0, MessageUtil.getMessage("operation.part.failed", null));
			}
			return new ErrorResponse(msg.toString());
		}
    	
    	// 删除db
		scheduleJobMapper.deleteBatch(ids);

		return new SuccessResponse(null);
	}

	/**
	 * 批量修改定时任务的状态
	 *
	 * @param ids
	 * @param status
	 * @return
	 */
	@Override
    public int updateBatch(List<Integer> ids, int status){
    	return scheduleJobMapper.updateBatch(ids, status);
    }

	/**
	 * 立即执行
	 *
	 * @param ids
	 * @return
	 */
	@Override
	public BaseResponse run(List<Integer> ids) {
		StringBuffer msg = new StringBuffer();

		// 取出所有的定时任务
		List<ScheduleJob> jobs = scheduleJobMapper.selectJobListByIds(ids);
		if(null == jobs || jobs.isEmpty()) {
			return new SuccessResponse(null);
		}

		int failedNum = 0;
    	for(Integer id : ids){
			ScheduleJob job = getJobByIdInList(jobs, id);
			if(null == job) {
				continue;
			}

			try {
				ScheduleUtils.run(scheduler, job);
			} catch (SchedulerException e) {
				log.error("run scheduler job error! ", e);

				failedNum++;

				// 拼接错误信息
				String[] name = {job.getBeanName() + SchedulerConstant.SYMBOL_DOT + job.getMethodName()};
				msg.append(MessageUtil.getMessage("scheduler.run.failed", name));
			}
		}

		if(failedNum > 0) {
			if(failedNum == jobs.size()) {
				// 全部失败
				log.error("run job all failed: {}", msg.toString());

				msg.setLength(0);
				msg.append(MessageUtil.getMessage("scheduler.run.all.failed", null));
			} else {
				// 部分失败
				msg.insert(0, MessageUtil.getMessage("operation.part.failed", null));
			}
			return new ErrorResponse(msg.toString());
		}

		return new SuccessResponse(null);
    }

	/**
	 * 暂停
	 *
	 * @param ids
	 * @return
	 */
	@Override
	public BaseResponse pause(List<Integer> ids) {
		StringBuffer msg = new StringBuffer();

		// 取出所有的定时任务
		List<ScheduleJob> jobs = scheduleJobMapper.selectJobListByIds(ids);
		if(null == jobs || jobs.isEmpty()) {
			return new SuccessResponse(null);
		}

		int failedNum = 0;
        for(Integer id : ids){
			ScheduleJob job = getJobByIdInList(jobs, id);
			if(null == job) {
				continue;
			}

			try {
				ScheduleUtils.pauseJob(scheduler, id);
			} catch (SchedulerException e) {
				log.error("pause scheduler job error! ", e);

				failedNum++;

				// 拼接错误信息
				String[] name = {job.getBeanName() + SchedulerConstant.SYMBOL_DOT + job.getMethodName()};
				msg.append(MessageUtil.getMessage("scheduler.pause.failed", name));

				// 去掉暂停失败的定时任务
				ids.remove(id);
			}
		}

		// 修改db
		updateBatch(ids, SchedulerConstant.ScheduleStatus.PAUSE.getValue());

        // 返回信息
		if(failedNum > 0) {
			if(failedNum == jobs.size()) {
				// 全部失败
				log.error("run job all failed: {}", msg.toString());

				msg.setLength(0);
				msg.append(MessageUtil.getMessage("scheduler.pause.all.failed", null));
			} else {
				// 部分失败
				msg.insert(0, MessageUtil.getMessage("operation.part.failed", null));
			}
			return new ErrorResponse(msg.toString());
		}

		return new SuccessResponse(null);
    }

	/**
	 * 恢复
	 *
	 * @param ids
	 * @return
	 */
	@Override
	public BaseResponse resume(List<Integer> ids) {
		StringBuffer msg = new StringBuffer();

		// 取出所有的定时任务
		List<ScheduleJob> jobs = scheduleJobMapper.selectJobListByIds(ids);
		if(null == jobs || jobs.isEmpty()) {
			return new SuccessResponse(null);
		}

		int failedNum = 0;
    	for(Integer id : ids){ScheduleJob job = getJobByIdInList(jobs, id);
			if(null == job) {
				continue;
			}

			try {
				ScheduleUtils.resumeJob(scheduler, id);
			} catch (SchedulerException e) {
				log.error("resume scheduler job error! ", e);

				failedNum++;

				// 拼接错误信息
				String[] name = {job.getBeanName() + SchedulerConstant.SYMBOL_DOT + job.getMethodName()};
				msg.append(MessageUtil.getMessage("scheduler.resume.failed", name));

				// 去掉暂停失败的定时任务
				ids.remove(id);
			}
		}

    	//  修改db
    	updateBatch(ids, SchedulerConstant.ScheduleStatus.NORMAL.getValue());

		// 返回信息
		if(failedNum > 0) {
			if(failedNum == jobs.size()) {
				// 全部失败
				log.error("run job all failed: {}", msg.toString());

				msg.setLength(0);
				msg.append(MessageUtil.getMessage("scheduler.resume.all.failed", null));
			} else {
				// 部分失败
				msg.insert(0, MessageUtil.getMessage("operation.part.failed", null));
			}
			return new ErrorResponse(msg.toString());
		}

		return new SuccessResponse(null);
    }

	/**
	 * 从list中根据ID获取定时任务
	 *
	 * @param jobs
	 * @param id
	 * @return
	 */
    private ScheduleJob getJobByIdInList(List<ScheduleJob> jobs, int id) {
		for(ScheduleJob job : jobs) {
			if(id == job.getId()) {
				return job;
			}
		}
		return null;
	}

	/**
	 * 输入校验
	 *
	 * @param job
	 * @return
	 */
	private String checkInput(ScheduleJob job) {
		StringBuffer msg = new StringBuffer();
		if(StringUtils.isEmpty(job.getBeanName())) {
			msg.append(MessageUtil.getMessage("scheduler.beanname.null", null));
		}

		if(StringUtils.isEmpty(job.getMethodName())) {
			msg.append(MessageUtil.getMessage("scheduler.methodname.null", null));
		}

		if(StringUtils.isEmpty(job.getCronExpression())) {
			msg.append(MessageUtil.getMessage("scheduler.cronexpression.null", null));
		} else {
			if(!isValidExpression(job.getCronExpression())) {
				msg.append(MessageUtil.getMessage("scheduler.cronexpression.invalid", null));
			}
		}
		return msg.toString();
	}

	/**
	 * 判断cron表达式是否有效
	 *
	 * @param cronExpression
	 * @return
	 */
	private boolean isValidExpression(final String cronExpression){
		CronTriggerImpl trigger = new CronTriggerImpl();
		try {
			trigger.setCronExpression(cronExpression);
			Date date = trigger.computeFirstFireTime(null);
			return date != null && date.after(new Date());
		} catch (Exception e) {
			log.error("cron expression error! " , e);
		}
		return false;
	}
    
}
