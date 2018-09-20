package com.vladkel.easy.youtube.download.model;

import static com.vladkel.easy.youtube.download.model.Dlr.Status.OK;

import lombok.Data;

/**
 * Bean of a youtude-dl response.
 * <p>
 * © Vladk-el 2018. All rights reserved.
 *
 * @author Eliott Laversin
 */
@Data
public class Dlr {

  private Long id;
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

  public Dlr(Hm hm) {
    setId(hm.getId());
    setStatus(OK);
    setContent(null);
    setName(hm.getName());
    setError(null);
  }

  public enum Status {
    OK, NOK
  }

}
