package com.lushu.checksystem;


import com.lushu.checksystem.util.WordUtil;
import com.xzixi.algorithm.simhash.analyzer.extractor.JcsegKeywordsExtractor;
import com.xzixi.algorithm.simhash.analyzer.hash.FVNHashGenerator;
import com.xzixi.algorithm.simhash.common.HashGenerator;
import com.xzixi.algorithm.simhash.common.KeywordsExtractor;
import com.xzixi.algorithm.simhash.core.SimHashUtil;
import com.xzixi.algorithm.simhash.core.SimHasher;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

/**
 * @author lushu
 * @date 19-11-11 下午10:07
 **/
class FileTest extends ChecksystemApplicationTests {

    @Test
    void simHashTest1(){
        KeywordsExtractor keywordsExtractor = new JcsegKeywordsExtractor();
        HashGenerator hashGenerator = new FVNHashGenerator();
        SimHasher simHasher = new SimHasher(keywordsExtractor, hashGenerator);
        BigInteger sign1 = simHasher.simhash("您的服务器111.111.111.111（ali-server01）存在异常登录行为：详情可登录云盾-安骑士控制台进行查看和处理，如果确认是您自己在登录。");
        BigInteger sign2 = simHasher.simhash("您的服务器111.111.111.111（ali-server03）存在异常登录行为：详情可登录云盾-安骑士控制台进行查看和处理，如果确认是您自己在登录，可忽略该短信。");
        System.out.println(String.format("distance: %d", SimHashUtil.getHammingDistance(sign1, sign2)));
    }

    @Test
    void simHashTest2() {
        KeywordsExtractor keywordsExtractor = new JcsegKeywordsExtractor();
        HashGenerator hashGenerator = new FVNHashGenerator();
        SimHasher simHasher = new SimHasher(keywordsExtractor, hashGenerator);
        BigInteger sign1 = simHasher.simhash(WordUtil.readWord("D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\word\\123.doc"));
        BigInteger sign2 = simHasher.simhash(WordUtil.readWord("D:\\FTP\\IntelliJ IDEA\\Projects\\checksystem\\src\\main\\resources\\word\\123.docx"));
        System.out.println("distance:"+SimHashUtil.getHammingDistance(sign1,sign2));
    }


}
