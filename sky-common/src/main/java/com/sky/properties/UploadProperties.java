package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.file")
@Data
public class UploadProperties {

    private String uploadDir;
    private String accessPath;
}
