package com.lushu.checksystem.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/3/20
 * @throws
 * @since
 */
@Data
public class LayuiDtree {
    private String id;
    private String title;
    private String field;
    private String href;
    private Boolean checked;
    private Boolean spread;
    private Boolean disabled;
    private List<LayuiDtree> children;
}
