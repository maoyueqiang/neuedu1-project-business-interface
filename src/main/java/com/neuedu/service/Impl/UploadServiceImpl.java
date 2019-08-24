package com.neuedu.service.Impl;

import com.google.gson.Gson;
import com.neuedu.common.ServerResponse;
import com.neuedu.service.IUploadService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class UploadServiceImpl implements IUploadService{

    @Autowired
    Auth auth;
    @Value("${qiniu.bucket}")
    private String bucketName;
    @Autowired
    UploadManager uploadManager;


    @Override
    public ServerResponse uploadFile(File uploadFile) {
        //生成上传凭证
        String uploadToken = auth.uploadToken(bucketName);

        try {
            //传文件,返回七牛云的response
            Response response = uploadManager.put(uploadFile,null,uploadToken);

            //解析上传结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);//上传至七牛云的图片的名称

            return ServerResponse.createServerResponseBySuccess(null,putRet.hash);
        } catch (QiniuException e) {
            e.printStackTrace();
        }


        return null;
    }
}
