package com.ztj.quartzdemo.service;

import com.ztj.quartzdemo.common.bean.BaseResponse;
import com.ztj.quartzdemo.model.ScheduleJob;

import java.util.List;
import java.util.Map;

public interface IScheduleJobService {

    BaseResponse selectByQuery(Map<String, Object> params);

    BaseResponse add(ScheduleJob scheduleJob);

    BaseResponse update(int id, ScheduleJob scheduleJob);

    BaseResponse deleteBatch(List<Integer> ids);

    int updateBatch(List<Integer> ids, int status);

    BaseResponse run(List<Integer> ids);

    BaseResponse pause(List<Integer> ids);

    BaseResponse resume(List<Integer> ids);

}
