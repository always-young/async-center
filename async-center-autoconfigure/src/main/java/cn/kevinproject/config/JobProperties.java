package cn.kevinproject.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Kevin Liu
 */
@ConfigurationProperties(
        prefix = "elastic.job"
)
@Data
public class JobProperties {

    private String serverList;
    private String namespace;
}
