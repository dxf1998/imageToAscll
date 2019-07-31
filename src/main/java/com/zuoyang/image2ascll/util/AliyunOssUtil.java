package com.zuoyang.image2ascll.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.zuoyang.image2ascll.config.ConstantConfig;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author  左羊
 * @date 2019-07-31 11:53:24
 */
@SuppressWarnings("AliDeprecation")
@Component("aliyunOSSUtil")
public class AliyunOssUtil {

    @Autowired
    private ConstantConfig constantConfig;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AliyunOssUtil.class);

    /**
     * 上传文件
     */
    @SuppressWarnings("AliDeprecation")
    public String upLoad(File file) {
        LOGGER.info("------OSS文件上传开始--------" + file.getName());
        String endpoint = constantConfig.getLXIMAGE_END_POINT();
        System.out.println("获取到的Point为:" + endpoint);
        String accessKeyId = constantConfig.getLXIMAGE_ACCESS_KEY_ID();
        String accessKeySecret = constantConfig.getLXIMAGE_ACCESS_KEY_SECRET();
        String bucketName = constantConfig.getLXIMAGE_BUCKET_NAME1();
        String fileHost = constantConfig.getLXIMAGE_FILE_HOST();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
        String fileGetUrl = null;
        // 判断文件
        if (file == null) {
            return null;
        }
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            // 判断容器是否存在,不存在就创建
            if (!client.doesBucketExist(bucketName)) {
                client.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                client.createBucket(createBucketRequest);
            }
            // 设置文件路径和名称
            String fileUrl = fileHost + "/" + (dateStr + "/" + UUID.randomUUID().toString().replace("-", "") + "-" + file.getName());
            System.out.println("A+fileUrl:" + fileUrl);
            // 上传文件
            PutObjectResult result = client.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            // 设置权限(公开读)
            client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (result != null) {
                LOGGER.info("------OSS文件上传成功------" + fileUrl);
                LOGGER.info("文件上传路径：" + "https://" + constantConfig.getLXIMAGE_BUCKET_NAME1() + "." + constantConfig.getLXIMAGE_END_POINT() + "/" + fileUrl);
                fileGetUrl = "https://" + constantConfig.getLXIMAGE_BUCKET_NAME1() + "." + constantConfig.getLXIMAGE_END_POINT() + "/" + fileUrl;
//                fileGetUrl = "https://" + constantConfig.getLXIMAGE_BUCKET_NAME1() + "." + constantConfig.getLXIMAGE_END_POINT() + "/" + fileUrl;
                return fileGetUrl;
            }
        } catch (OSSException oe) {
            LOGGER.error(oe.getMessage());
        } catch (ClientException ce) {
            LOGGER.error(ce.getErrorMessage());
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }

        return null;
    }

}
