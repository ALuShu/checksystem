package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;

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
  private BigInteger sign;
  /**
   * 海明距离{"文件名":1000001010101}
   */
  private HashMap<String, Integer> distances;


}
