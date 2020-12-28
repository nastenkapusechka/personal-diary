package com.nastenkapusechka.mypersonaldiary.repo;

import com.nastenkapusechka.mypersonaldiary.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findByUserUsername(String username);
}
