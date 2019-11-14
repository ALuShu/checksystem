package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class Teacher implements Serializable {

  private BigInteger id;
  private String name;
  private String password;
  private String department;


}
