package com.lushu.checksystem.util;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.lushu.checksystem.constant.OtherConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.ConnectException;

/**
 * 参考：https://www.cnblogs.com/ph7seven/p/10158489.html
 *
 * @author ALuShu
 * @Description
 * @date 2020/5/1
 */
@Slf4j
public class Doc2PdfUtil {

    private String openOfficeHost;
    private Integer openOfficePort;

    public Doc2PdfUtil() {
    }

    public Doc2PdfUtil(String openOfficeHost, Integer openOfficePort) {
        this.openOfficeHost = openOfficeHost;
        this.openOfficePort = openOfficePort;
    }


    /**
     * doc转pdf
     * @return pdf文件路径
     */
    public File doc2Pdf(StringBuffer fileName,String name) throws ConnectException {
        File docFile = new File(fileName.toString());
        int pointIndex = fileName.lastIndexOf(".");
        if (name.endsWith("doc")){
            name = name.replaceAll("doc","pdf");
        }else {
            name = name.replaceAll("docx","pdf");
        }
        File pdfFile = new File(OtherConstant.REALPATH + "临时文件文件夹" + File.separator + name);
        if (docFile.exists()) {
            if (!pdfFile.exists()) {
                OpenOfficeConnection connection = new SocketOpenOfficeConnection(openOfficeHost, openOfficePort);
                try {
                    connection.connect();
                    DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
                    //最核心的操作，doc转pdf
                    converter.convert(docFile, pdfFile);
                    connection.disconnect();
                    log.info("****pdf转换成功，PDF输出：" + pdfFile.getPath() + "****");
                } catch (java.net.ConnectException e) {
                    log.info("****pdf转换异常，openoffice服务未启动！****");
                    e.printStackTrace();
                    throw e;
                } catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
                    System.out.println("****pdf转换器异常，读取转换文件失败****");
                    e.printStackTrace();
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        } else {
            log.info("****pdf转换异常，需要转换的doc文档不存在，无法转换****");
        }
        return pdfFile;
    }
}
