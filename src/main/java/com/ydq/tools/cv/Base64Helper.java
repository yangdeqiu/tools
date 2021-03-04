package com.ydq.tools.cv;

import org.springframework.util.Base64Utils;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Base64Helper {

    /**
     * 文件转base64
     * @Param path文件地址
     * @Return java.lang.String  返回编码后的字符串
     */
    public static String encodeBase64File(String path){
        // 读取文件
        File file = new File(path);
        // 声明输入流
        FileInputStream inputFile = null;
        // 声明字节数组
        byte[] buffer = null;
        try {
            inputFile = new FileInputStream(file);
            // 创建字节数组
            buffer = new byte[(int) file.length()];
            // 输入
            inputFile.read(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inputFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 解码
        return Base64Utils.encodeToString(buffer);
    }

    public static void main(String[] args) {
        String path = "/Users/mac/Desktop/tmp/IMG_0114.mp4";
        System.out.println(encodeBase64File(path));
    }
}
