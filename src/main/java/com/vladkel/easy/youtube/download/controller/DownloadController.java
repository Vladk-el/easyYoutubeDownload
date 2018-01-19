package com.vladkel.easy.youtube.download.controller;

import com.vladkel.easy.youtube.download.model.Dl;
import com.vladkel.easy.youtube.download.model.Dlr;
import com.vladkel.easy.youtube.download.model.Hm;
import com.vladkel.easy.youtube.download.service.DownloadService;
import com.vladkel.easy.youtube.download.service.HistoryService;
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
  private DownloadService downloadService;

  @Autowired
  private HistoryService historyService;

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
    return downloadService.downloadOnServer(dl);
  }

  /**
   * Donwload HTTP endpoint.
   * Allow to retrieve a music by given name.
   *
   * @param id the identifier of wanted file.
   * @return a {@link Dlr} response object.
   */
  @GetMapping("/download/{id}")
  public ResponseEntity<Resource> download(@PathVariable(value = "id") Long id)
      throws IOException {

    LOGGER.info("Download request for {} intercepted.", id);

    File file = downloadService.download(id);
    Path path = Paths.get(file.getAbsolutePath());
    ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Disposition", "attachment; filename=\"" + path.getFileName().toString() + "\"");
    headers.set("Content-Length", String.valueOf(file.length()));

    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(file.length())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }

  /**
   * History HTTP endpoint.
   *
   * @return a list of all previous download.
   * @throws IOException if a problem come during list process.
   */
  @GetMapping("/history")
  public @ResponseBody List<Hm> getHistory() throws IOException {
    return historyService.getHistory();
  }
}
