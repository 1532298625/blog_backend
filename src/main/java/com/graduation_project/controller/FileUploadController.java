package com.graduation_project.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.graduation_project.utils.UploadUtils;
import com.graduation_project.vo.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileUploadController {


    @PostMapping
    public ResponseResult uploadImages(@RequestParam("image") MultipartFile file) {
        String name = file.getOriginalFilename();

        String fileName =UUID.randomUUID() + name.substring(name.lastIndexOf("."));
//        String fileName =UUID.randomUUID() + name;
//        System.out.println(fileName);
        try {
            String url = UploadUtils.upload(file, fileName);
            if (StringUtils.isNotBlank(url)) {
                return ResponseResult.success("上传成功过",url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(UploadUtils.getURl()+fileName);
        return ResponseResult.fail("上传失败");
    }
}
