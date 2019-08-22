package com.neuedu.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UploadController {

    @RequestMapping()
    public String upload(){
        //约定优于配置
        //直接去templates下找upload.html文件
        return "upload";
    }


}
