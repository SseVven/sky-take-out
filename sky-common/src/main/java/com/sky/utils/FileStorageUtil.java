package com.sky.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileStorageUtil {
    private String uploadDir;
    private String accessPath;

    public String storeFile(MultipartFile file) throws IOException {
        // 确保目录存在
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 保存文件
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 生成静态资源访问路径
        String url = "http://192.168.137.100:8080" +
                accessPath.substring(0, accessPath.indexOf('*')) +
                fileName;
        return url;
    }
}
