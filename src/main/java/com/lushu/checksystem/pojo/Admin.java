package com.lushu.checksystem.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Admin implements Serializable {

  private Long id;
  private String password;


}
