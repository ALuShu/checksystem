package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class Admin implements Serializable {

  private BigInteger id;
  private String password;


}
