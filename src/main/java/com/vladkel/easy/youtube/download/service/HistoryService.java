package com.vladkel.easy.youtube.download.service;

import com.vladkel.easy.youtube.download.model.Hr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * History service.
 * Allow to crawl passed downloads.
 * <p>
 * © Vladk-el 2017. All rights reserved.
 *
 * @author elaversin
 */
@Service
public class HistoryService {

  private final static Logger LOGGER = LoggerFactory.getLogger(HistoryService.class);

  @Value("${download.base.path}")
  private String DOWNLOAD_BASE_PATH;

  public List<Hr> getHistory() throws IOException {
    List<Hr> files = Files.list(Paths.get(DOWNLOAD_BASE_PATH)).filter(Files::isRegularFile)
        .filter(p -> p.toString().endsWith(".mp3"))
        .map(p -> new Hr(new File(p.toUri())))
        .collect(Collectors.toList());
    return files;
  }

}
