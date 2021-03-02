package cn.kevinproject.job;

import cn.kevinproject.task.TaskExecutor;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.google.common.collect.Maps;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author Kevin Liu
 */
@Component
public class JobRegister implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Map<String, JobTemplate> jobMaps = Maps.newConcurrentMap();

    private final TaskExecutor taskExecutor;

    private final ZookeeperRegistryCenter zookeeperRegistryCenter;

    public JobRegister(TaskExecutor taskExecutor,ZookeeperRegistryCenter zookeeperRegistryCenter) {
        this.taskExecutor = taskExecutor;
        this.zookeeperRegistryCenter = zookeeperRegistryCenter;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init(){
        final Map<String, JobTemplate> jobTemplateMap = applicationContext.getBeansOfType(JobTemplate.class);
        jobTemplateMap.values().forEach(t->{
            if(StringUtils.isEmpty(t.getJobName())) {
                throw new RuntimeException("jobName can't be null");
            }
            jobMaps.put(t.getJobName(),t);
            registerElasticJob(t.getJobName());
        });
    }

    public void registerElasticJob(String jobName) {
        String cron = "0/1 * * * * ?";
        val coreConfig = JobCoreConfiguration
                .newBuilder(jobName,cron, 10)
                .build();
        val jobConfig = new DataflowJobConfiguration(coreConfig, AsyncJob.class.getCanonicalName(), true);
        val liteJobConfig = LiteJobConfiguration.newBuilder(jobConfig).overwrite(true).build();

        JobScheduler jobScheduler = new JobScheduler(zookeeperRegistryCenter, liteJobConfig);

        jobScheduler.init();
    }

}
