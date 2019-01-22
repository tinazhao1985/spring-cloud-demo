package com.ztj.quartzdemo.controller;

import com.ztj.quartzdemo.common.bean.BaseResponse;
import com.ztj.quartzdemo.model.ScheduleJob;
import com.ztj.quartzdemo.service.IScheduleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 定时任务
 * 
 */
@Controller
@RequestMapping("job")
public class ScheduleJobController {

	@Autowired
	private IScheduleJobService scheduleJobService;

	/**
	 * 查询列表
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/page",method = RequestMethod.GET)
	@ResponseBody
	public BaseResponse list(@RequestParam Map<String, Object> params){
		return scheduleJobService.selectByQuery(params);
	}

	/**
	 * 新增定时任务
	 *
	 * @param scheduleJob
	 * @return
	 */
	@RequestMapping(value = "",method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse add(@RequestBody ScheduleJob scheduleJob){
		return scheduleJobService.add(scheduleJob);
	}

	/**
	 * 修改定时任务
	 *
	 * @param id
	 * @param scheduleJob
	 * @return
	 */
	@RequestMapping(value = "/{id}",method = RequestMethod.PUT)
	@ResponseBody
	public BaseResponse update(@PathVariable int id, @RequestBody ScheduleJob scheduleJob){
		return scheduleJobService.update(id, scheduleJob);
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/batchDel",method = RequestMethod.DELETE)
	@ResponseBody
	public BaseResponse delete(@RequestBody List<Integer> ids){
		return scheduleJobService.deleteBatch(ids);
	}

	/**
	 * 立即执行任务
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/run",method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse run(@RequestBody List<Integer> ids){
		return scheduleJobService.run(ids);
	}

	/**
	 * 暂停定时任务
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/pause",method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse pause(@RequestBody List<Integer> ids){
		return scheduleJobService.pause(ids);
	}

	/**
	 * 恢复定时任务
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/resume",method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse resume(@RequestBody List<Integer> ids){
		return scheduleJobService.resume(ids);
	}

}
