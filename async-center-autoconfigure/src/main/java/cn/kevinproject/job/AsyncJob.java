package cn.kevinproject.job;

import cn.kevinproject.task.RedisTaskExecutor;
import cn.kevinproject.task.TaskExecutor;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Kevin Liu
 */
@Slf4j
public class AsyncJob implements DataflowJob<Long>  {

    private TaskExecutor taskExecutor;

    private TaskExecutor getTaskExcecutor() {
        if (taskExecutor == null) {
            taskExecutor = RedisTaskExecutor.taskExecutor;
            if (taskExecutor == null) {
                log.info("wait for task manager instance");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
                taskExecutor = getTaskExcecutor();
            }
        }
        return taskExecutor;
    }

    @Override
    public List<Long> fetchData(ShardingContext shardingContext) {
        return Optional.ofNullable(getTaskExcecutor().getExecData(shardingContext.getJobName()))
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
    }

    @Override
    public void processData(ShardingContext shardingContext, List<Long> list) {
        list.forEach(taskId -> getTaskExcecutor().executeTask(taskId));
    }

}
