package org.example.languagemaster.repository;

import org.example.languagemaster.entity.QuizzesResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizzesResultsRepository extends JpaRepository<QuizzesResults, Long> {}
