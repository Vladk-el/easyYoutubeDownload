package com.vladkel.easy.youtube.download.service;

import com.vladkel.easy.youtube.download.model.Dl;
import com.vladkel.easy.youtube.download.model.Dlr;
import com.vladkel.easy.youtube.download.model.Hr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
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

  private final static Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);

  private final static String DESTINATION = "[ffmpeg] Destination: ";
  private final static String DOWNLOAD_DONE = "[download] 100%";
  private final static String ALREADY_DOWNLOADED = "has already been downloaded";

  @Value("${download.base.path}")
  private String DOWNLOAD_BASE_PATH;

  public Dlr downloadOnServer(Dl dl) {
    LOGGER.info("try do download : {}", dl.getUrl());
    return feedback(parse(dl));
  }

  private String parse(Dl dl) {
    return new StringBuilder("youtube-dl")
        .append(" --no-playlist")
        .append(" --restrict-filenames")
        .append(" --no-mtime")
        .append(" --extract-audio")
        .append(" --audio-format mp3")
        .append(" -o " + DOWNLOAD_BASE_PATH + "/%(title)s.%(ext)s ")
        .append(dl.getUrl())
        .toString();
  }

  private Dlr feedback(String cmd) {

    LOGGER.info("cmd -> {}", cmd);

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
    // Status TODO
    Dlr.Status status =
        content.contains(DOWNLOAD_DONE) && !content.contains(ALREADY_DOWNLOADED) ?
            Dlr.Status.OK :
            Dlr.Status.NOK;

    // Path
    String name = null;
    List<String> paths = Pattern.compile("\\n").splitAsStream(content)
        .filter(s -> s.contains(DESTINATION)).collect(
            Collectors.toList());
    if (paths.size() > 0) {
      String path = DOWNLOAD_BASE_PATH + paths.get(0).substring(paths.get(0).indexOf(DESTINATION));
      name = path.substring(path.lastIndexOf("/") + 1);
    }

    // Error TODO
    String error = null;

    return new Dlr(status, content, name, error);
  }

  public File download(String name) {
    String path = DOWNLOAD_BASE_PATH + "/" + name;
    LOGGER.debug("looking for file {}", path);
    return new File(path);
  }

  public List<Hr> list() throws IOException {
    List<Hr> files = Files.list(Paths.get(DOWNLOAD_BASE_PATH)).filter(Files::isRegularFile)
        .map(p -> new Hr(new File(p.toUri())))
        .collect(Collectors.toList());
    return files;
  }

}
