package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Data
public class File implements Serializable {

  private BigInteger id;
  private String name;
  private Float size;
  private Date updateTime;
  private Integer type;
  private String permission;
  private BigInteger owner;
  private Integer status;


}
