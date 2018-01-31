package com.vladkel.easy.youtube.download.configuration.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic and personalized security context.
 * Allows to secure the app with login(s) & password(s).
 * Load automatically from property file / program arguments with prefix "security".
 * <p>
 * © Vladk-el 2018. All rights reserved.
 *
 * @author Eliott Laversin
 */
@Component
@ConfigurationProperties("security")
public class SecurityContext {

  private Boolean enabled = false;

  private List<User> users = new ArrayList<>();

  public Boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enable) {
    this.enabled = enable;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  @Override
  public String toString() {
    return "SecurityContext {" +
        "enabled='" + enabled + '\'' +
        ", users=" + users +
        '}';
  }


  public static class User {

    private String name;
    private String password;
    private ROLE role;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public ROLE getRole() {
      return role;
    }

    public void setRole(ROLE role) {
      this.role = role;
    }

    @Override
    public String toString() {
      return "User {" +
          "name='" + name + '\'' +
          ", password='" + password + '\'' +
          ", role='" + role + '\'' +
          '}';
    }

    public enum ROLE {
      USER,
      ADMIN
    }
  }
}
