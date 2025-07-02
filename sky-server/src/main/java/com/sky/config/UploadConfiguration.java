package com.sky.config;

import com.sky.properties.UploadProperties;
import com.sky.utils.FileStorageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class UploadConfiguration {

    /**
     * 在配置类里注入一下 upload bean
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public FileStorageUtil fileStorageUtil(UploadProperties uploadProperties) {
        log.info("开始创建本地存储工具类对象:{}", uploadProperties);
        return new FileStorageUtil(uploadProperties.getUploadDir(),
                uploadProperties.getAccessPath());
    }
}
