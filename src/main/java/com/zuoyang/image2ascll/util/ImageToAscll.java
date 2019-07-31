package com.zuoyang.image2ascll.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author  左羊
 * @date 2019-07-31 11:23:01
 *
 */
public class ImageToAscll {

    /**
     * @param path
     *            图片路径
     */
    public static String createAsciiPic(final String path) {
        final String base = "#8XOHLTI)i=+;:,. ";// 字符串由复杂到简单
        final String txtPath ="/data/image2ascll/txts/abc.txt";
        try {
            final BufferedImage image = ImageIO.read(new File(path));  //读取图片
            //输出到指定文件中
            final BufferedWriter fos = new BufferedWriter(new FileWriter(txtPath,false));   //true表示是否追加
            for (int y = 0; y < image.getHeight(); y += 2) {
                for (int x = 0; x < image.getWidth(); x++) {
                    final int pixel = image.getRGB(x, y);
                    final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                    final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                    final int index = Math.round(gray * (base.length() + 1) / 255);
                    String s = index >= base.length() ? " " : String.valueOf(base.charAt(index));
                    fos.write(s);
                }
                fos.newLine();
            }
            fos.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return txtPath;
    }

}
