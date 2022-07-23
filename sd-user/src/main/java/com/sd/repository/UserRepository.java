package com.sd.repository;

import com.sd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author D
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);

  /**
   * Find by username and is deleted false.
   *
   * @param username the username
   * @return the {@link Optional#empty()} if no result, otherwise return the optional of user
   */
  Optional<User> findByUsernameAndIsDeletedFalse(String username);
}
