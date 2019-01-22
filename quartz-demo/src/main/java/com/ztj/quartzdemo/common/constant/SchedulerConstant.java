package com.ztj.quartzdemo.common.constant;

public class SchedulerConstant {

    public final static String JOB_NAME = "TASK_";

    public final static String SYMBOL_DOT = ".";

    /**
     * 定时任务执行成功
     */
    public final static int EXECUTE_SUCCESS = 0;

    /**
     * 定时任务执行失败
     */
    public final static int EXECUTE_FAILED = 1;

    /**
     * 定时任务状态
     *
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        private ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
