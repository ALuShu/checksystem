package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Inform implements Serializable {

  private Integer id;
  private Integer sendId;
  private Integer receiveId;
  private String content;


}
