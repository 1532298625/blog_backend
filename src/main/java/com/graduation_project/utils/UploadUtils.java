package com.graduation_project.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadUtils {
    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    private final static String endpoint = "oss-cn-shenzhen.aliyuncs.com";
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    private final static String accessKeyId = "LTAI5tK6ocMk9xgkL5bAnoUq";
    private final static String accessKeySecret = "8hzUifwknyrw7QOAuo5UNtRw7Zp4Ye";
    // 填写Bucket名称，graduation_project-blog。
    private final static String bucketName = "blog-project-dobby";

    public static String getURl() {
        return "https://"+bucketName+"."+endpoint+"/";
    }

    public static String upload(MultipartFile file,String fileName ) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "BLOG-APP/images/"+date+"/"+fileName;


        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build("https://"+endpoint, accessKeyId, accessKeySecret);

        try {
            // 填写Byte数组。
            byte[] content = file.getBytes();
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return null;
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return "https://"+bucketName+"."+endpoint+"/"+objectName;
    }
}