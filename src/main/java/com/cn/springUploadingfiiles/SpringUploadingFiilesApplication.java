package com.cn.springUploadingfiiles;

import com.cn.springUploadingfiiles.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SpringUploadingFiilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringUploadingFiilesApplication.class, args);
    }

}
