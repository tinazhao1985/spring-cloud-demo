package com.ztj.jwtdemo.listener;

import com.ztj.jwtdemo.service.IDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 应用启动成功后加载数据
 *
 */
@Component
@Slf4j
public class DataListener implements ApplicationListener<ApplicationReadyEvent> {

    private static int ENTER_COUNT = 0;

    @Autowired
    private IDictService dcitService;

    /**
     * 应用启动成功后加载数据字典到缓存
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event)
    {
        log.info("enter into ApplicationReadyEvent. ");

        if(ENTER_COUNT == 0) {
            // 加载数据字典到缓存
            dcitService.getDictList();
        }
        ENTER_COUNT++;
        log.info("ENTER_COUNT = " + ENTER_COUNT);

    }
}
