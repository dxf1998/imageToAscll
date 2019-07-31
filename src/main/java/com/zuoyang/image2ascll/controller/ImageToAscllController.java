package com.zuoyang.image2ascll.controller;

import com.zuoyang.image2ascll.service.imp.ImageToAscllServiceImp;
import com.zuoyang.image2ascll.util.AliyunOssUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
/**
 * @author  douxiaofeng
 * @date 2019-07-31 11:53:24
 */

@RestController
public class ImageToAscllController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ImageToAscllServiceImp imageToAscllServiceImp;
    @Autowired
    private AliyunOssUtil aliyunOSSUtil;

    @RequestMapping(value = "/uploadimg")
    @PostMapping
    public @ResponseBody
    String imgUploa(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        logger.info("文件上传");
        String filename = file.getOriginalFilename();
        logger.info("文件路径 = {}",filename);

        String uploadUrl = null;
        String uploadTxtUrl = null;
        String txtName = null;
        try {
            if (file != null) {
                if (!"".equals(filename.trim())) {
                    // 图片
                    File newFile = new File("/data/image2ascll/txts/"+filename);
                    FileOutputStream os = new FileOutputStream(newFile);
                    os.write(file.getBytes());
                    os.close();
                    file.transferTo(newFile);
                    // 图片上传到OSS
                    uploadUrl = file.getOriginalFilename();
                    logger.info("图片地址={}",uploadUrl);
                    // 文字
                     txtName = imageToAscllServiceImp.imageToAscll(uploadUrl);
                    File newTxtFile = new File(txtName);
                    // 上传到OSS
                    uploadTxtUrl = aliyunOSSUtil.upLoad(newTxtFile);

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return uploadTxtUrl;
    }
}
