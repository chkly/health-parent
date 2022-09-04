package com.java.backend.controller;

import com.java.common.constant.MessageConstant;
import com.java.common.entity.Result;
import com.java.utils.QiniuUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("qiniu")
@Api("七牛云文件上传删除测试")
public class QiniuController {

    @Autowired
    private QiniuUtil qiniuUtil;

    @PostMapping("/upload")
    public Result upload(@RequestParam("file")MultipartFile multipartFile) {
        try {
            byte[] bytes = multipartFile.getBytes();
            String originalFilename = multipartFile.getOriginalFilename();
            qiniuUtil.uploadToQiniu(bytes,originalFilename);
            return  new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @DeleteMapping("/deleteImg")
    public Result upload(String fileName){
            qiniuUtil.deleteFromQiniu(fileName);
            return new Result(true,"图片删除成功");
    }

}
