package com.vladkel.easy.youtube.download.controller;

import com.vladkel.easy.youtube.download.model.Dl;
import com.vladkel.easy.youtube.download.model.Dlr;
import com.vladkel.easy.youtube.download.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * © Vladk-el 2017. All rights reserved.
 *
 * @author elaversin
 */
@Controller
public class DownloadController {

  @Autowired
  private DownloadService service;

  @RequestMapping("/")
  public String index() {
    return "index";
  }

  @PostMapping("/download")
  public @ResponseBody Dlr download(@RequestBody Dl dl) {
    return service.download(dl);
  }
}
