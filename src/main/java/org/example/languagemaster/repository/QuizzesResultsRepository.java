package org.example.languagemaster.repository;

import org.example.languagemaster.entity.QuizzesResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface QuizzesResultsRepository extends JpaRepository<QuizzesResults, Long> {
    @Query(value = """
    SELECT *
    FROM quizzes_results
    WHERE user_id = :userId
      AND quizze_id IN (=:quizzeIds)
""", nativeQuery = true)
    List<QuizzesResults> getAllByUserIdAndQuizzeId(
            @Param("userId") Long userId,
            @Param("topicId") Set<Long> quizzeIds
    );
}
