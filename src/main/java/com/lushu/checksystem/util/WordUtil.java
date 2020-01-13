package com.lushu.checksystem.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

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
                log.debug("此文件不是word文件！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //new
        return buffer;
    }

    public void testReadByDoc(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        HWPFDocument doc = new HWPFDocument(is);
        //输出书签信息
        this.printInfo(doc.getBookmarks());
        //输出文本
        log.info(doc.getDocumentText());
        Range range = doc.getRange();
        this.printInfo(range);
        //读表格
        this.readTable(range);
        //读列表
        this.readList(range);
        //把当前HWPFDocument写到输出流中
        doc.write(new FileOutputStream("D:\\test.doc"));
        is.close();
    }

    /**
     * 输出书签信息
     *
     * @param bookmarks
     */
    private void printInfo(Bookmarks bookmarks) {
        int count = bookmarks.getBookmarksCount();
        log.info("书签数量：" + count);
        Bookmark bookmark;
        for (int i = 0; i < count; i++) {
            bookmark = bookmarks.getBookmark(i);
            log.info("书签" + (i + 1) + "的名称是：" + bookmark.getName());
            log.info("开始位置：" + bookmark.getStart());
            log.info("结束位置：" + bookmark.getEnd());
        }
    }

    /**
     * 读表格
     * 每一个回车符代表一个段落，所以对于表格而言，每一个单元格至少包含一个段落，每行结束都是一个段落。
     *
     * @param range
     */
    private void readTable(Range range) {
        //遍历range范围内的table。
        TableIterator tableIter = new TableIterator(range);
        Table table;
        TableRow row;
        TableCell cell;
        while (tableIter.hasNext()) {
            table = tableIter.next();
            int rowNum = table.numRows();
            for (int j = 0; j < rowNum; j++) {
                row = table.getRow(j);
                int cellNum = row.numCells();
                for (int k = 0; k < cellNum; k++) {
                    cell = row.getCell(k);
                    //输出单元格的文本
                    log.info(cell.text().trim());
                }
            }
        }
    }

    /**
     * 读列表
     *
     * @param range
     */
    private void readList(Range range) {
        int num = range.numParagraphs();
        Paragraph para;
        for (int i = 0; i < num; i++) {
            para = range.getParagraph(i);
            if (para.isInList()) {
                log.info("list: " + para.text());
            }
        }
    }

    /**
     * 输出Range
     *
     * @param range
     */
    private void printInfo(Range range) {
        //获取段落数
        int paraNum = range.numParagraphs();
        System.out.println(paraNum);
        for (int i = 0; i < paraNum; i++) {
            log.info("段落" + (i + 1) + "：" + range.getParagraph(i).text());
        }
        int secNum = range.numSections();
        System.out.println(secNum);
        Section section;
        for (int i = 0; i < secNum; i++) {
            section = range.getSection(i);
            System.out.println(section.getMarginLeft());
            System.out.println(section.getMarginRight());
            System.out.println(section.getMarginTop());
            System.out.println(section.getMarginBottom());
            System.out.println(section.getPageHeight());
            System.out.println(section.text());
        }
    }
}
