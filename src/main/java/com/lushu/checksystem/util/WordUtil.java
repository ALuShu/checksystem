package com.lushu.checksystem.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import java.io.FileInputStream;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/13
 * @throws
 * @since
 */
@Slf4j
public class WordUtil {

    private static final String DOC = "doc";
    private static final String DOCX = "docx";


    public static String readWord(String path) {
        String buffer = "";
        try {
            if (path.endsWith(DOC)) {
                FileInputStream is = new FileInputStream(path);
                WordExtractor ex = new WordExtractor(is);
                buffer = ex.getText();
                is.close();
            } else if (path.endsWith(DOCX)) {
                OPCPackage opcPackage = POIXMLDocument.openPackage(path);
                POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
                buffer = extractor.getText();
                opcPackage.close();
            } else {
                log.info("word工具类：此文件不是word文件！");
            }

        } catch (Exception e) {
            log.info("word工具类：未知异常！"+e);
        }
        //new
        return buffer;
    }

}
