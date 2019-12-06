package org.koko.kokoprojectone.models.data;

import org.koko.kokoprojectone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserDao extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String userName);

}
