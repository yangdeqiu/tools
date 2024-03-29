package com.ydq.tools.filesystem.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * 大文件拆分、重组工具方法
 */
public class FileDataHandler {

    private static final int SIZE_4K = 1024 << 2;

    /**
     * 大文件拆分合并
     * 文件重组
     *
     * @param fileList    正确顺序文件列表
     * @param combinePath 重组文件输出路径
     */
    private static void combine(List<String> fileList, String combinePath) {
        try (
                FileOutputStream out = new FileOutputStream(combinePath)
        ) {
            byte[] data = new byte[SIZE_4K];
            for (String fn : fileList) {
                RandomAccessFile raf = new RandomAccessFile(fn, "r");
                int rest = (int) (raf.length() % (SIZE_4K));
                long rest_pointer = raf.length() - rest;
                while (true) {
                    if (raf.getFilePointer() == rest_pointer) {
                        byte[] restData = new byte[rest];
                        raf.read(restData);
                        out.write(restData);
                        break;
                    } else {
                        raf.read(data);
                        out.write(data);
                    }
                }
                raf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件切片，必须使用raf否则文件重组后会出现问题
     *
     * @param filepath 文件路径
     * @param n        文件拆分数量
     * @throws IOException
     */
    private static void split(String filepath, int n) throws IOException {
        File file = new File(filepath);
        try (
                RandomAccessFile raf = new RandomAccessFile(file, "r")
        ) {
            long size = file.length();
            long step = size / n;
            byte[] data = new byte[SIZE_4K];
            for (int i = 0; i < n; i++) {
                long start = i * step;
                long end = (i != n - 1) ? (i + 1) * step : size;
                int rest = (int) ((end - start) % (SIZE_4K));
                long rest_pointer = end - rest; 
                FileOutputStream out = new FileOutputStream(filepath + i);
                raf.seek(start);
                while (true) {
                    // 拆分后的数据可能不是data容量的整数倍，在非最后一块数据时，不能直接读取data容量数据
                    if (raf.getFilePointer() == rest_pointer) {
                        byte[] restData = new byte[rest];
                        raf.read(restData);
                        out.write(restData);
                        break;
                    } else {
                        raf.read(data);
                        out.write(data);
                    }
                }
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
    }
}
