package org.example.languagemaster.repository;

import jakarta.validation.constraints.PastOrPresent;
import org.example.languagemaster.entity.GrammarTopics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarRepository extends JpaRepository<GrammarTopics, Long> {
    List<GrammarTopics> findAllByOrderByIdAsc();
}
