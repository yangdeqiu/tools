package com.ydq.tools.document.poihelper;

import com.ydq.tools.filesystem.file.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;

public class excelHelper {

    public static void main(String[] args) {
        try {
            exportExcel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成excel
     */
    public static void exportExcel() throws IOException {
        //1.在内存中创建一个excel文件
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        //2.创建工作簿
        HSSFSheet sheet = hssfWorkbook.createSheet();
        //3.创建标题行
        HSSFRow titlerRow = sheet.createRow(0);
        titlerRow.createCell(0).setCellValue("省");
        titlerRow.createCell(1).setCellValue("市");
        titlerRow.createCell(2).setCellValue("区");
        titlerRow.createCell(3).setCellValue("邮编");
        titlerRow.createCell(4).setCellValue("简码");
        titlerRow.createCell(5).setCellValue("城市编码");

        //4.遍历数据,创建数据行
//        for (Area area : list) {
//            //获取最后一行的行号
//            int lastRowNum = sheet.getLastRowNum();
//            HSSFRow dataRow = sheet.createRow(lastRowNum + 1);
//            dataRow.createCell(0).setCellValue(area.getProvince());
//            dataRow.createCell(1).setCellValue(area.getCity());
//            dataRow.createCell(2).setCellValue(area.getDistrict());
//            dataRow.createCell(3).setCellValue(area.getPostcode());
//            dataRow.createCell(4).setCellValue(area.getShortcode());
//            dataRow.createCell(5).setCellValue(area.getCitycode());
//        }
        //5.创建文件名
        String fileName = "/Users/mac/Desktop/区域数据统计.xls";

//        //6.获取输出流对象
//        HttpServletResponse response = ServletActionContext.getResponse();
//        ServletOutputStream outputStream = response.getOutputStream();
//
//        //7.获取mimeType
//        ServletContext servletContext = ServletActionContext.getServletContext();
//        String mimeType = servletContext.getMimeType(fileName);
//        //8.获取浏览器信息,对文件名进行重新编码
//        HttpServletRequest request = ServletActionContext.getRequest();
//        fileName = FileUtils.filenameEncoding(fileName, request);

        FileOutputStream outputStream = new FileOutputStream(fileName);

//        //9.设置信息头
//        response.setContentType(mimeType);
//        response.setHeader("Content-Disposition","attachment;filename="+fileName);
        //10.写出文件,关闭流
        hssfWorkbook.write(outputStream);
        hssfWorkbook.close();
    }

}
