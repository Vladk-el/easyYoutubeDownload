package com.vladkel.easy.youtube.download;

import com.vladkel.easy.youtube.download.configuration.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * © Vladk-el 2018. All rights reserved.
 *
 * @author Eliott Laversin
 */
@SpringBootApplication
@EnableConfigurationProperties(SecurityContext.class)
public class Application {

  @Autowired
  private SecurityContext securityContext;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
