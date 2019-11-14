package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class File implements Serializable {

  private Long id;
  private String name;
  private Double size;
  private java.sql.Timestamp updateTime;
  private Long type;
  private String permission;
  private Long owner;
  private Long status;


}
