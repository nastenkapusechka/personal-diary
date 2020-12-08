package com.nastenkapusechka.mypersonaldiary.repo;

import com.nastenkapusechka.mypersonaldiary.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
