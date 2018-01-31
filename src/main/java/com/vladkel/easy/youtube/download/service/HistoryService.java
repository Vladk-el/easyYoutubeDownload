package com.vladkel.easy.youtube.download.service;

import com.vladkel.easy.youtube.download.model.Hm;
import com.vladkel.easy.youtube.download.persistence.HmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * History service.
 * Allow to manage passed downloads.
 * <p>
 * © Vladk-el 2018. All rights reserved.
 *
 * @author Eliott Laversin
 */
@Service
public class HistoryService {

  private final static Logger LOGGER = LoggerFactory.getLogger(HistoryService.class);

  @Autowired
  private HmRepository repository;

  public List<Hm> getHistory() {
    return repository.findByOrderByIdDesc();
  }

}
