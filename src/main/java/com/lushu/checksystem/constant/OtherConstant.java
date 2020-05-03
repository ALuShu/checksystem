package com.lushu.checksystem.constant;

import org.springframework.util.ClassUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ALuShu
 * @Description
 * @date 2020/2/12
 * @throws
 * @since
 */
public final class OtherConstant {
    /**
     * 日期格式化器
     */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * Excel表格表头解析
     */
    public static final Map<String, String> SHEET_HEAD = new HashMap<>();
    public static final String REALPATH;

    static {
        SHEET_HEAD.put("姓名", "realname");
        SHEET_HEAD.put("学号", "username");
        SHEET_HEAD.put("系别", "department");
        SHEET_HEAD.put("专业", "major");
        SHEET_HEAD.put("工号", "username");

        REALPATH = ClassUtils.getDefaultClassLoader().getResource("").getPath()
                .replaceAll("%20", " ") + "root";
    }

}
