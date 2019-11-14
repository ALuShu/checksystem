package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class Inform implements Serializable {

  private BigInteger id;
  private BigInteger sendId;
  private BigInteger receiveId;
  private String content;


}
