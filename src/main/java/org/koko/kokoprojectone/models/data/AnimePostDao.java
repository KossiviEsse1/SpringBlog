package org.koko.kokoprojectone.models.data;

import org.koko.kokoprojectone.models.AnimePost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AnimePostDao extends CrudRepository<AnimePost,Integer> {
}
