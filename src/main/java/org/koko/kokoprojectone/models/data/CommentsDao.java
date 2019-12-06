package org.koko.kokoprojectone.models.data;

import org.koko.kokoprojectone.models.Comments;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CommentsDao extends CrudRepository<Comments, Integer> {
}
