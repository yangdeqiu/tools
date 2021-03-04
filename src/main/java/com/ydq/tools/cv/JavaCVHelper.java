package com.ydq.tools.cv;

import org.apache.logging.log4j.util.Base64Util;
import org.bytedeco.javacv.*;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaCVHelper {

    public static List<Frame> extractImage(String base64File) throws FrameGrabber.Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64Utils.decodeFromString(base64File));
        FFmpegFrameGrabber fg = new FFmpegFrameGrabber(in, 0);
        FFmpegLogCallback.set();
        fg.setFormat("mp4");
        fg.start();
        int len = fg.getLengthInFrames(), interval = new Double(fg.getFrameRate()).intValue();
        List<Frame> frameList = new ArrayList<>();
        Frame frame;
        for (int i = 0; i < len; i ++) {
            frame = fg.grabImage();
            if (i%interval == 0) {
                doExecuteFrame(frame, "/Users/mac/Desktop/tmp/jpg/", "frame", i);
                frameList.add(frame);
            }
        }
        return frameList;
    }

    public static void doExecuteFrame(Frame f, String targerFilePath, String targetFileName, int index) {
        if (null == f || null == f.image) {
            return;
        }

        Java2DFrameConverter converter = new Java2DFrameConverter();

        String imageMat = "jpg";
        String FileName = targerFilePath + File.separator + targetFileName + "_" + index + "." + imageMat;
        BufferedImage bi = converter.getBufferedImage(f);
        File output = new File(FileName);
        try {
            ImageIO.write(bi, imageMat, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FrameGrabber.Exception {
        String path = "/Users/mac/Desktop/tmp/IMG_0114.mp4";
        extractImage(Base64Helper.encodeBase64File(path));
    }

}
