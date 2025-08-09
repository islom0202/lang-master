package org.example.languagemaster.repository;

import org.example.languagemaster.entity.Levels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelRepository extends JpaRepository<Levels, Long> {

}
