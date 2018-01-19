package com.vladkel.easy.youtube.download.persistence;

import com.vladkel.easy.youtube.download.model.Hm;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * HM repository. Allow to interact with db.
 * <p>
 * © Vladk-el 2017. All rights reserved.
 *
 * @author elaversin
 */
public interface HmRepository extends CrudRepository<Hm, Long> {

  Hm findById(Long id);

  Hm findByCleanName(String cleanName);

  List<Hm> findByOrderByIdDesc();

}
