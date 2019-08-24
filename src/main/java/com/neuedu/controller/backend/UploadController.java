package com.neuedu.controller.backend;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/manage/product/")
public class UploadController {

    @RequestMapping(value = "upload",method = RequestMethod.GET)
    public String upload(){
        //约定优于配置
        //直接去templates下找upload.html文件
        return "upload";
    }

    @Value("${img.local.path}")
    private String imgPath;
    @Autowired
    IUploadService uploadService;

    @RequestMapping(value = "upload",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(@RequestParam("picfile")MultipartFile uploadFile){
        if(uploadFile!=null&&uploadFile.getOriginalFilename()!=null&&!uploadFile.getOriginalFilename().equals("")){
            String uuid = UUID.randomUUID().toString();//生成新字符串(文件的新名)
            String filename = uploadFile.getOriginalFilename();//原文件名，包括扩展名
            String fileextendname = filename.substring(filename.lastIndexOf("."));//扩展名
            String newfilename=uuid+fileextendname;//新全名

            //文件保存目录
            File file = new File(imgPath);
            if(!file.exists()){
                file.mkdir();
            }
            File newFile = new File(file,newfilename);
            try {
                //将文件写入到磁盘中
                uploadFile.transferTo(newFile);

                //将图片上传到七牛云
                return uploadService.uploadFile(newFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
            //上传七牛云之前用的
            //return ServerResponse.createServerResponseBySuccess(newFile.getName());

        }
        return ServerResponse.createServerResponseByFail("请输入正确的文件名");
    }


}
