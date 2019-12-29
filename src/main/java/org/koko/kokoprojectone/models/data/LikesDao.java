package org.koko.kokoprojectone.models.data;

import org.koko.kokoprojectone.models.Likes;
import org.koko.kokoprojectone.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface LikesDao extends CrudRepository<Likes, Integer> {
}
