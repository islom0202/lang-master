package org.example.languagemaster.repository;

import org.example.languagemaster.entity.VocabularyWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyWordsRepository extends JpaRepository<VocabularyWords, Long> {
    List<VocabularyWords> findAllByGroupsId_Id(Long id);
}
