package com.nastenkapusechka.mypersonaldiary.repo;

import com.nastenkapusechka.mypersonaldiary.entities.Secret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretRepository extends JpaRepository<Secret, Long> {

    Secret findByTitle(String title);

}
