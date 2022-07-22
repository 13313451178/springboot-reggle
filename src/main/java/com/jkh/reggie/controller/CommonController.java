package com.jkh.reggie.controller;

import com.jkh.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

//文件上传下载的实现
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String bathPath;
    /*文件上传*/
    @PostMapping ("/upload")
    public R<String> upload(MultipartFile file){
        log.info("菜品上传照片");
        log.info("bathPath"+bathPath);
        File dir = new File(bathPath);
        if(!dir.exists()){
            /*目录不存在 创建*/
            dir.mkdirs();
        }
        String fileName=UUID.randomUUID().toString()+".jpg";
        try {
            file.transferTo(new File(bathPath+ fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);

    }
    /*文件下载*/
    @GetMapping("/download")
    public void downLoad(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(bathPath+name));
            /*向浏览器回写数据*/
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes=new byte[1024];
            int len=0;
//            回写图片
            response.setContentType("image/jpeg");
            while((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
