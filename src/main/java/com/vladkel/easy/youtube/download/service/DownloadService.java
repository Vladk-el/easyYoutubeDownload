package com.vladkel.easy.youtube.download.service;

import static com.vladkel.easy.youtube.download.model.Dlr.Status.OK;

import com.vladkel.easy.youtube.download.model.Dl;
import com.vladkel.easy.youtube.download.model.Dlr;
import com.vladkel.easy.youtube.download.model.Hm;
import com.vladkel.easy.youtube.download.persistence.HmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Download service.
 * Allow to download a video from Youtube & store it on the server.
 * <p>
 * © Vladk-el 2018. All rights reserved.
 *
 * @author Eliott Laversin
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

  @Autowired
  private HmRepository repository;

  public Dlr downloadOnServer(Dl dl) {
    LOGGER.info("trying do download : {} ...", dl.getUrl());
    List<Hm> existing = repository.findByUrl(dl.getUrl());
    if (!existing.isEmpty()) {
      Hm last = existing.get(existing.size() - 1);
      LOGGER.info("This file was already downloaded. Let's reuse old entry {}.", last.getId());
      return new Dlr(last);
    }
    return process(dl);
  }

  private Dlr process(Dl dl) {
    Date start = new Date();
    String cmd = getCmd(dl);

    LOGGER.info("cmd -> {}", cmd);

    Process p;
    String result = null;
    String error = null;
    try {
      p = Runtime.getRuntime().exec(cmd);
      p.waitFor();

      result = read(p.getInputStream());
      LOGGER.debug("[youtube] => \n{}", result);

      if (result.endsWith(PREMATURE_ENDS)) {
        result = read(p.getErrorStream());
        throw new Exception(result);
      }

    } catch (Exception e) {
      error = e.getMessage();
      LOGGER.error("Error during youtube-dl execution : " + error, e);
    }

    return saveHm(dl, getDlr(result, error), start);
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
            OK :
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

  private Dlr saveHm(Dl dl, Dlr dlr, Date start) {
    if (dlr.getStatus().equals(OK)) {

      List<Hm> existing = repository.findByCleanName(dlr.getName());
      if (existing.isEmpty()) {
        repository.save(
            new Hm(
                dl,
                dlr,
                new File(DOWNLOAD_BASE_PATH + "/" + dlr.getName())
            )
        );
        dlr.setId(repository.findByCleanName(dlr.getName()).get(0).getId());
        LOGGER
            .info("Took {} seconds to download {}", (new Date().getTime() - start.getTime()) / 1000,
                dlr.getName());
      } else {
        long last = existing.get(existing.size() - 1).getId();
        LOGGER.info("This file was already downloaded. Let's reuse old entry {}.", last);
        dlr.setId(last);
      }

    }
    return dlr;
  }

  public File download(Long id) {
    Hm hm = repository.findById(id);
    String path = DOWNLOAD_BASE_PATH + "/" + hm.getCleanName();
    LOGGER.info("Found file {} for index {}.", path, id);
    return new File(path);
  }

}
