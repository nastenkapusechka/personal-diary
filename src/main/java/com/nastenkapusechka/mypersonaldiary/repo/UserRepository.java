package com.nastenkapusechka.mypersonaldiary.repo;

import com.nastenkapusechka.mypersonaldiary.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
