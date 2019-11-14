package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class Student implements Serializable {

  private BigInteger id;
  private String name;
  private String password;
  private String department;
  private String major;


}
