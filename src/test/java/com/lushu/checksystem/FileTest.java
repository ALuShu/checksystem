package com.lushu.checksystem;


import com.lushu.checksystem.util.SimHash;
import com.lushu.checksystem.util.WordUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author lushu
 * @date 19-11-11 下午10:07
 **/
class FileTest extends ChecksystemApplicationTests {

    /*通常认为海明距离小于3的为高度相似文本*/
    @Test
    void simHashTest1(){
        String str1 = "您的服务器111.111.111.111（ali-server01）存在异常登录行为：详情可登录云盾-安骑士控制台进行查看和处理，如果确认是您自己在登录。";
        String str2 = "您的服务器111.111.111.111（ali-server01）存在异常登录行为：详情可登录云盾-安骑士控制台进行查看和处理，如果确认是您自己在登录。";
        SimHash sign1 = null;
        SimHash sign2 = null;
        try {
            sign1 = new SimHash(str1, 64);
            sign2 = new SimHash(str2, 64);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int dis12 = sign1.getDistance(sign1.strSimHash, sign2.strSimHash);
        System.out.println(sign1.strSimHash);
        System.out.println(sign2.strSimHash);
        System.out.println(dis12);
    }

    @Test
    void simHashTest2() {
        String str1 = WordUtil.readWord("D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\word\\123.doc");
        String str2 = WordUtil.readWord("D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\word\\123.docx");
        SimHash sign1 = null;
        SimHash sign2 = null;
        try {
            sign1 = new SimHash(str1, 64);
            sign2 = new SimHash(str2, 64);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int dis12 = sign1.getDistance(sign1.strSimHash, sign2.strSimHash);
        System.out.println(sign1.strSimHash);
        System.out.println(sign2.strSimHash);
        System.out.println(dis12);
    }


}
