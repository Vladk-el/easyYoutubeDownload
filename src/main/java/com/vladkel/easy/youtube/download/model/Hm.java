package com.vladkel.easy.youtube.download.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.File;
import java.util.Date;

/**
 * Bean of stored history entry.
 * <p>
 * © Vladk-el 2018. All rights reserved.
 *
 * @author Eliott Laversin
 */
@Entity
public class Hm {

  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String cleanName;
  private String url;
  private long length;
  private Date date;

  /**
   * Default constructor for JPA.
   */
  public Hm() {
  }

  public Hm(Dl dl, Dlr dlr, File file) {
    this.cleanName = dlr.getName();
    this.name = dlr.getName()
        .replaceAll("_", " ")
        .replaceAll(".mp3", "");
    this.url = dl.getUrl();
    this.length = file.length();
    this.date = new Date(file.lastModified());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCleanName() {
    return cleanName;
  }

  public void setCleanName(String cleanName) {
    this.cleanName = cleanName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public long getLength() {
    return length;
  }

  public void setLength(long length) {
    this.length = length;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
