package com.vladkel.easy.youtube.download.model;

import org.springframework.beans.factory.annotation.Value;

/**
 * © Vladk-el 2017. All rights reserved.
 * <p>
 * Bean of a youtude-dl response.
 *
 * @author elaversin
 */
public class Dlr {

  private Status status;
  private String content;
  private @Value("${download.base.path}") String path;
  public enum Status {
    OK, NOK
  }

  public Dlr(Status status, String content, String path) {
    this.status = status;
    this.content = content;
    this.path = path;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
