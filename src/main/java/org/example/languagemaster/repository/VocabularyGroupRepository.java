package org.example.languagemaster.repository;

import org.example.languagemaster.entity.VocabularyGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyGroupRepository extends JpaRepository<VocabularyGroups, Long> {}
