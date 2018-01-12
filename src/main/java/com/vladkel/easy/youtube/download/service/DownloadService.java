package com.vladkel.easy.youtube.download.service;

import com.vladkel.easy.youtube.download.model.Dl;
import com.vladkel.easy.youtube.download.model.Dlr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * © Vladk-el 2017. All rights reserved.
 *
 * @author elaversin
 */
@Service
public class DownloadService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);

  @Value("${download.base.path}")
  private String DOWNLOAD_BASE_PATH;

  public Dlr download(Dl dl) {
    LOGGER.info("try do download : {}", dl.getUrl());
    return feedback(parse(dl));
  }

  private String parse(Dl dl) {
    return new StringBuilder("youtube-dl")
        .append(" --no-playlist")
        .append(" --restrict-filenames")
        .append(" --audio-quality 0")
        .append(" -o " + DOWNLOAD_BASE_PATH + "/%(title)s.%(ext)s ")
        .append(dl.getUrl())
        .toString();
  }

  private Dlr feedback(String cmd) {

    LOGGER.info("cmd : {}", cmd);

    StringBuffer output = new StringBuffer();

    Process p;
    try {
      p = Runtime.getRuntime().exec(cmd);
      p.waitFor();

      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line = "";
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    LOGGER.info("feedback : {}" + output.toString());

    return getDlr(output.toString());
  }

  private Dlr getDlr(String content) {
    Dlr.Status status =
        content.contains("[download] 100%") && !content.contains("has already been downloaded") ?
            Dlr.Status.OK :
            Dlr.Status.NOK;
    List<String> paths = Pattern.compile("\\n").splitAsStream(content)
        .filter(s -> s.contains("[download] Destination:")).collect(
            Collectors.toList());
    return new Dlr(status, content, DOWNLOAD_BASE_PATH + (paths.size() > 0 ? paths.get(0) : ""));
  }

}
