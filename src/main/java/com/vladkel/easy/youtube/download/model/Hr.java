package com.vladkel.easy.youtube.download.model;

import java.io.File;
import java.util.Date;

/**
 * © Vladk-el 2017. All rights reserved.
 *
 * @author elaversin
 */
public class Hr {

  private String name;
  private float length;
  private Date date;

  public Hr(File file) {
    this.name = file.getName();
    this.length = file.length();
    this.date = new Date(file.lastModified());
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getLength() {
    return length;
  }

  public void setLength(float length) {
    this.length = length;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

}
