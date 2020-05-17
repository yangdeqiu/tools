package com.ydq.tools.document.transform;

import com.ydq.tools.common.ToolsConst;
import com.ydq.tools.document.docconst.DocumentType;
import com.ydq.tools.filesystem.file.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.util.Scanner;

/**
 * pdf转换类
 */
@Slf4j
public class PDFTransformer<main> {

    /**
     * 将pwd转化为word输出
     *
     * @param pdfIn 输入的pdf路径
     */
    public static boolean pdf2word(String pdfIn) {
        File pdfFile = new File(pdfIn);
        if(!pdfFile.exists()) {
            log.error("=== pdf文件:{}不存在 ===", pdfIn);
            return false;
        }
        String pdfName = getPdfName(pdfIn);
        String wordName = getWordFileName(pdfName);
        File wordFile = new File(wordName);
        if(!wordFile.exists()){
            if(!FileUtils.forceCreateNewFile(wordFile)){
                log.error("=== pdf转换目标word文件创建失败 ==");
                return false;
            }
        }
        try(
                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(wordFile), ToolsConst.DEFAULT_CHARACTOR_UTF8))
                ){
            PDDocument pdoc = PDDocument.load(pdfFile);
            int pageNum = pdoc.getNumberOfPages();
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            stripper.setEndPage(pageNum);
            stripper.writeText(pdoc, bw);
            pdoc.close();
            log.info("=== pdf文件转换word成功 ===");
            return true;
        } catch (IOException e) {
            log.error("=== pdf文件:{}转换word失败 ===", pdfIn);
            e.printStackTrace();
            return false;
        }
    }

    private static String getWordFileName(String wordOut) {
        return !(wordOut.endsWith(DocumentType.TYPE_WORD_DOC)
                || wordOut.endsWith(DocumentType.TYPE_WORD_DOCX))
                    ? wordOut + DocumentType.TYPE_WORD_DOC
                    : wordOut;
    }

    private static String getPdfName(String pdfIn){
        return pdfIn.endsWith(DocumentType.TYPE_PWF)
                ? pdfIn.substring(0, pdfIn.lastIndexOf(DocumentType.TYPE_PWF))
                : pdfIn;
    }


}
