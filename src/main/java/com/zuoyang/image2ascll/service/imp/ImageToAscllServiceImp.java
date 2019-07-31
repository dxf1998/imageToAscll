package com.zuoyang.image2ascll.service.imp;

import com.zuoyang.image2ascll.service.ImageToAscllServie;
import com.zuoyang.image2ascll.util.ImageToAscll;
import com.zuoyang.image2ascll.util.ImageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author  左羊
 * @date 2019-07-31 11:53:24
 */
@Service
public class ImageToAscllServiceImp implements ImageToAscllServie {
    @Override
    public String imageToAscll(String path) {
        InputStream in = null;
        //缩放后需要保存的路径
        File saveFile = new File("/data/image2ascll/txts/ABC.jpg");
        try {
            //原图片的路径
            in = new FileInputStream(new File("/data/image2ascll/txts/"+path));
            if(ImageHelper.compress(in, saveFile, 5)){
                System.out.println("图片压缩5倍！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

       return ImageToAscll.createAsciiPic("/data/image2ascll/txts/ABC.jpg");
    }
}
