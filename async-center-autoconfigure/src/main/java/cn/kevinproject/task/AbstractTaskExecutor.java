package cn.kevinproject.task;

import com.google.common.collect.Maps;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

/**
 * @author Kevin Liu
 */
public abstract class AbstractTaskExecutor implements TaskExecutor{

    private Map<String, Queue<Long>> queues = Maps.newConcurrentMap();

    /**
     * 初始化queue
     * @param jobName jobName
     * @return queue
     */
    public abstract Queue<Long> initQueue(String jobName);

    @PostConstruct
    public void init() {
        doInit();
    }

    protected abstract void doInit();

    @Override
    public Queue<Long> getTaskExecutorQueue(String jobName) {
        return Optional.ofNullable(queues.get(jobName)).orElse(initQueue(jobName));
    }

    @Override
    public void registerTask(Long taskId,String jobName) {
        getTaskExecutorQueue(jobName).add(taskId);
    }

    @Override
    public Long getExecData(String jobName) {
        return getTaskExecutorQueue(jobName).poll();
    }
}
