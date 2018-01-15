package com.vladkel.easy.youtube.download.controller;

import com.vladkel.easy.youtube.download.model.Dl;
import com.vladkel.easy.youtube.download.model.Dlr;
import com.vladkel.easy.youtube.download.model.Hr;
import com.vladkel.easy.youtube.download.service.DownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * © Vladk-el 2017. All rights reserved.
 *
 * @author elaversin
 */
@Controller
public class DownloadController {

  private final static Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);

  @Autowired
  private DownloadService service;

  /**
   * Index mapping.
   *
   * @return the index.html template.
   */
  @RequestMapping("/")
  public String index() {
    return "index";
  }

  /**
   * DownloadOnServer HTTP endpoint.
   * Allow to download music & store it on the server.
   *
   * @param dl the {@link Dl} object build by front.
   * @return a {@link Dlr} response object.
   */
  @PostMapping("/downloadOnServer")
  public @ResponseBody Dlr downloadOnServer(@RequestBody Dl dl) {
    return service.downloadOnServer(dl);
  }

  /**
   * Donwload HTTP endpoint.
   * Allow to retrieve a music by given name.
   *
   * @param fileName the name of wanted file.
   * @return a {@link Dlr} response object.
   */
  @GetMapping("/download/{fileName:.+}")
  public ResponseEntity<Resource> download(@PathVariable(value = "fileName") String fileName)
      throws IOException {

    LOGGER.info("Download request for {}", fileName);

    File file = service.download(fileName);
    Path path = Paths.get(file.getAbsolutePath());
    ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

    HttpHeaders headers = new HttpHeaders();
    headers.set("content-disposition", "inline; finename=\"" + file.getName() + "\"");
    headers.set("content-length", String.valueOf(file.length()));

    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(file.length())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }

  /**
   * List HTTP endpoint.
   *
   * @return a list of all previous download.
   * @throws IOException if a problem come during list process.
   */
  @GetMapping("/list")
  public @ResponseBody List<Hr> list() throws IOException {
    return service.list();
  }
}
