package com.vladkel.easy.youtube.download.model;

import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@Entity
@NoArgsConstructor
public class Hm {

  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String cleanName;
  private String url;
  private long length;
  private Date date;

  public Hm(Dl dl, Dlr dlr, File file) {
    this.cleanName = dlr.getName();
    this.name = dlr.getName()
        .replaceAll("_", " ")
        .replaceAll(".mp3", "");
    this.url = dl.getUrl();
    this.length = file.length();
    this.date = new Date(file.lastModified());
  }

}
