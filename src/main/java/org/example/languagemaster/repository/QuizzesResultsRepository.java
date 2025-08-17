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
      AND quizze_id IN (:quizzeIds)
""", nativeQuery = true)
    List<QuizzesResults> getAllByUserIdAndQuizzeId(
            @Param("userId") Long userId,
            @Param("quizzeIds") List<Long> quizzeIds
    );

    @Query(value = """
            select quizze_id from quizzes_results
            where quizze_id in (
            select id from quizzes where section_id =:sectionId and section_type =:sectionType) and user_id =:userId;
            """, nativeQuery = true)
    List<Long> selectedQuizzes(@Param("sectionId") Long sectionId,
                                         @Param("sectionType") String sectionType,
                                         @Param("userId") Long userId);
}
