package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.FileStorageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private FileStorageUtil fileStorageutil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);
        try {
            if (file.isEmpty()) {
                return Result.error("文件为空");
            }

            // 验证文件类型
            String contentType = file.getContentType();
            log.info("文件类型：{}", contentType);
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error("仅支持图片文件");
            }

            // 保存文件
            String fileUrl = fileStorageutil.storeFile(file);
            log.info("返回图片路径：{}", fileUrl);
            return Result.success(fileUrl);

        } catch (IOException e) {
            return Result.error(MessageConstant.UPLOAD_FAILED + ": " + e.getMessage());
        }
    }
}
