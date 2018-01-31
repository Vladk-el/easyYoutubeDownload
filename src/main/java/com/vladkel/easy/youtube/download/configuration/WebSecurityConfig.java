package com.vladkel.easy.youtube.download.configuration;

import com.vladkel.easy.youtube.download.configuration.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * © Vladk-el 2018. All rights reserved.
 *
 * @author Eliott Laversin
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final static Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

  @Autowired
  private SecurityContext securityContext;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    if (securityContext.isEnabled()) {
      LOGGER.info("Configure HttpSecurity mapping ...");
      http
          .authorizeRequests()
          .antMatchers("/", "/downloadOnServer", "/download/{id}", "/history").permitAll()
          .anyRequest().authenticated()
          .and()
          .formLogin()
          .loginPage("/login").defaultSuccessUrl("/", true)
          .permitAll()
          .and()
          .logout()
          .permitAll();
    }
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    if (securityContext.isEnabled() && !securityContext.getUsers().isEmpty()) {
      for (SecurityContext.User user : securityContext.getUsers()) {
        LOGGER.info("Configure AuthenticationManager for {}.", user);
        auth
            .inMemoryAuthentication()
            .withUser(user.getName()).password(user.getPassword()).roles(user.getRole().toString());
      }
    }
  }

}
