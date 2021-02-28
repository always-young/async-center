package cn.kevinproject.config;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kevin Liu
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties({JobProperties.class})
public class AsyncCenterAutoConfiguration {

    @Autowired
    private JobProperties jobProperties;

    @Bean(
            initMethod = "init"
    )
    @ConditionalOnMissingBean({ZookeeperRegistryCenter.class})
    public ZookeeperRegistryCenter coordinatorRegistryCenter(JobProperties properties) {
        return new ZookeeperRegistryCenter(new ZookeeperConfiguration(properties.getServerList(), properties.getNamespace()));
    }
}
