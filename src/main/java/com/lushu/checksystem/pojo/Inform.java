package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Inform implements Serializable {

  private Integer id;
  private String title;
  private String publisher;
  private String department;
  private String content;
  private String date;
  private String path;
  private Integer type;

}
