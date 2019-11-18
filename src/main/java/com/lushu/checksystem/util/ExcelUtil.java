package com.lushu.checksystem.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel表格解析工具类
 * @author lushu
 * @date 2019/11/14 22:54
 **/
public class ExcelUtil {

    /**
     * 记录excel表格的表头
     */
    private static final Map<String, String> sheetHead;
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    static {
        sheetHead = new HashMap<>();
        sheetHead.put("姓名","name");
        sheetHead.put("学号","id");
        sheetHead.put("系别","department");
        sheetHead.put("专业","major");
        sheetHead.put("工号","id");
    }
    public static List explain(MultipartFile file) throws IOException{
        String filename = file.getOriginalFilename();
        if (filename == null || "".equals(filename)){
            throw new RuntimeException("文件名为空");
        }

        if(!filename.endsWith(XLS) && !filename.endsWith(XLSX)){
            throw new IOException(filename + "不是excel文件");
        }

        FileInputStream in = new FileInputStream(filename);
        if (filename.endsWith(XLS)){
            HSSFWorkbook workbook = new HSSFWorkbook(in);
            for (int s = 0; s < workbook.getNumberOfSheets(); s++){
                HSSFSheet sheet = workbook.getSheetAt(s);//表对象
                HSSFRow row = sheet.getRow(0);//第一行表头信息
                HSSFCell headCell = null;
                for(int headIndex = 0; headIndex < row.getLastCellNum(); headIndex++){
                    headCell = row.getCell(headIndex);
                    if (sheetHead.containsKey(headCell)){
                        //写beanutil代码
                    }
                }
            }


        }else {
            XSSFWorkbook workbook = new XSSFWorkbook(in);




        }

    }


}
