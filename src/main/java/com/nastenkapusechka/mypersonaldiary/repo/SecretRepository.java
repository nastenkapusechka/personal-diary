package com.nastenkapusechka.mypersonaldiary.repo;

import com.nastenkapusechka.mypersonaldiary.entities.Secret;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecretRepository extends CrudRepository<Secret, Long> {
    @Modifying
    @Query("update Secret s set s.title = ?1, s.content = ?2 where s.id = ?3")
    void updateSecret(String title, String content, Long id);

    List<Secret> findByUserUsername(String username);
}
