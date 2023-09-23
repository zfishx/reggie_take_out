package com.itheima.reggie_take_out.controller;

import com.itheima.reggie_take_out.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

//文件上传和下载
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basepath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //file是一个临时文件，需要转存到指定位置，本次请求完成后临时文件会被删除
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString()+suffix;
        //创建目录对象
        File dir = new File(basepath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        file.transferTo(new File(basepath+filename));
        //log.info(file.toString());
        return R.success(filename);
    }
    //文件下载
    @GetMapping("/download")
    public void download(String name, HttpServletResponse resp) throws IOException {
        //输入流，通过输入流读取文件内容
        FileInputStream fis = new FileInputStream(new File(basepath+name));
        //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
        ServletOutputStream sos = resp.getOutputStream();
        int len=0;
        byte[] buffer = new byte[1024];
        while(len != -1){
            len = fis.read(buffer);
            sos.write(buffer,0,len);
            sos.flush();
        }
        //关闭流
        sos.close();
        fis.close();
        //设置返回的文件类型为图片
        resp.setContentType("image/jpeg");
    }
}
