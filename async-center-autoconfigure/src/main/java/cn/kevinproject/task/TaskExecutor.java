package cn.kevinproject.task;

import java.util.Queue;

/**
 * 任务注册接口
 *
 * @author Kevin Liu
 */
public interface TaskExecutor {

    /**
     * 获取
     *
     * @return queue
     */
    Queue<Long> getTaskExecutorQueue(String jobName);


    /**
     * 注册一个任务
     *
     * @param taskId  taskId
     * @param jobName jobName
     */
    void registerTask(Long taskId, String jobName);

    /**
     * 获取待执行的任务
     *
     * @param jobName jobName
     * @return taskId
     */
    Long getExecData(String jobName);

    void executeTask(Long taskId);
}
