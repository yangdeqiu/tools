package com.ydq.tools.filesystem.file;

import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 文件操作工具
 */
public class FileUtils {

    public static String filenameEncoding(String filename, HttpServletRequest request) throws IOException {
        String agent = request.getHeader("User-Agent"); //获取浏览器
        if (agent.contains("Firefox")) {
            BASE64Encoder base64Encoder = new BASE64Encoder();
            filename = "=?utf-8?B?"
                    + base64Encoder.encode(filename.getBytes("utf-8"))
                    + "?=";
        } else if(agent.contains("MSIE")) {
            filename = URLEncoder.encode(filename, "utf-8");
        } else if(agent.contains ("Safari")) {
            filename = new String (filename.getBytes ("utf-8"),"ISO8859-1");
        } else {
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    public static boolean forceCreateNewFile(File worFile) {
        try {
            if (!worFile.exists()) {
                File dir = worFile.getParentFile();
                if (!dir.exists()) dir.mkdirs();
                worFile.createNewFile();
            }
        }catch (IOException e){
            return false;
        }
        return true;
    }
}
