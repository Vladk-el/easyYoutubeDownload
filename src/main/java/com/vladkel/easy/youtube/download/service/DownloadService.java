package com.vladkel.easy.youtube.download.service;

import com.vladkel.easy.youtube.download.model.Dl;
import com.vladkel.easy.youtube.download.model.Dlr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Download service.
 * Allow to download a video from Youtube & store it on the server.
 * <p>
 * © Vladk-el 2017. All rights reserved.
 *
 * @author elaversin
 */
@Service
public class DownloadService {

  private final static Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);

  private final static String DESTINATION = "[ffmpeg] Destination: ";
  private final static String DOWNLOAD_DONE = "[download] 100%";
  private final static String LINE_SEPARATOR = System.getProperty("line.separator");
  private final static String PREMATURE_ENDS = "Downloading video info webpage" + LINE_SEPARATOR;

  @Value("${download.base.path}")
  private String DOWNLOAD_BASE_PATH;

  public Dlr downloadOnServer(Dl dl) {
    LOGGER.info("trying do download : {} ...", dl.getUrl());
    return process(getCmd(dl));
  }

  private String getCmd(Dl dl) {
    return new StringBuilder("youtube-dl")
        .append(" --no-playlist")
        .append(" --restrict-filenames")
        .append(" --no-mtime")
        .append(" --extract-audio")
        .append(" --audio-format mp3").append(" -o ").append(DOWNLOAD_BASE_PATH)
        .append("/%(title)s.%(ext)s ")
        .append(dl.getUrl())
        .toString();
  }

  private Dlr process(String cmd) {

    LOGGER.info("cmd -> {}", cmd);

    Process p;
    String result = null;
    String error = null;
    try {
      p = Runtime.getRuntime().exec(cmd);
      p.waitFor();

      result = read(p.getInputStream());

      if (result.endsWith(PREMATURE_ENDS)) {
        result = read(p.getErrorStream());
        throw new Exception(result);
      }

    } catch (Exception e) {
      error = e.getMessage();
      LOGGER.error("Error during youtube-dl execution : " + error, e);
    }

    LOGGER.info("process : {}", result);

    return getDlr(result, error);
  }

  private String read(InputStream stream) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    StringBuilder output = new StringBuilder();
    String line = "";
    while ((line = reader.readLine()) != null) {
      output.append(line).append(LINE_SEPARATOR);
    }
    return output.toString();
  }

  private Dlr getDlr(String content, String error) {

    if (error != null) {
      return new Dlr(Dlr.Status.NOK, null, null, error);
    }

    // Status
    Dlr.Status status =
        content.contains(DOWNLOAD_DONE) ?
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

    return new Dlr(status, content, name, error);
  }

  public File download(String name) {
    String path = DOWNLOAD_BASE_PATH + "/" + name;
    LOGGER.info("looking for file {}", path);
    return new File(path);
  }

}
