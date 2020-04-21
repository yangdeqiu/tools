package com.ydq.tools.filesystem.file;

import java.io.File;
import java.io.IOException;

/**
 * 文件操作工具
 */
public class FileUtils {

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
