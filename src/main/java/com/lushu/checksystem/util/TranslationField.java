package com.lushu.checksystem.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lushu
 * @date 2019/11/21 17:19
 **/
public final class TranslationField {
    private static final Map<String, String> sheetHead;
    static {
        sheetHead = new HashMap<>();
        sheetHead.put("姓名","name");
        sheetHead.put("学号","id");
        sheetHead.put("系别","department");
        sheetHead.put("专业","major");
        sheetHead.put("工号","id");
    }

    public static Map<String, String> getSheetHead() {
        return sheetHead;
    }
}
