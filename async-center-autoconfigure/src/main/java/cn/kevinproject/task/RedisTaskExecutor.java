package cn.kevinproject.task;

import org.redisson.RedissonQueue;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.springframework.stereotype.Component;

import java.util.Queue;

/**
 * @author Kevin Liu
 */
@Component
public class RedisTaskExecutor extends AbstractTaskExecutor{

    private final RedissonClient redissonClient;

    public static TaskExecutor taskExecutor;

    public RedisTaskExecutor(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }



    @Override
    public Queue<Long> initQueue(String jobName) {
        return redissonClient.getQueue(jobName,new LongCodec());
    }

    @Override
    protected void doInit() {
        taskExecutor = this;
    }
}
