package com.vladkel.easy.youtube.download.model;

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
  private String name;
  private String error;

  public Dlr(Status status, String content, String name, String error) {
    this.status = status;
    this.content = content;
    this.name = name;
    this.error = error;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public enum Status {
    OK, NOK
  }
}
