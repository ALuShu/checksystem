package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class File implements Serializable {

  private Integer id;
  /**
   * 文件名
   */
  private String name;
  /**
   * 文件真实物理路径
   */
  private String path;
  private Long size;
  private String updateTime;
  /**
   * 文件类型，dir或doc、docx
   */
  private Integer type;
  private String permission;
  private Integer owner;
  private Integer submitter;
  private Integer status;
  private String sign;
  /**
   * 用来包装拥有者或提交人信息
   */
  private List<User> user;
  /**
   * 海明距离{{"filename":"text.docx"},{"distance":1000001010101}}
   */
  private List<HaiMingDistance> distances;

}
