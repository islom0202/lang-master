package org.example.languagemaster.repository;

import java.util.List;
import java.util.Set;

import org.example.languagemaster.entity.Quizzes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quizzes, Long> {
    @Query(value =
    "select * from quizzes where section_id =:topicId and section_type =:type",
    nativeQuery = true)
    List<Quizzes> allQuizByTopicIdAndType(@Param("topicId") Long topicId,
                                          @Param("type") String type);

    @Query(value =
            "select * from quizzes where section_id =:topicId and id not in (:ids)",
            nativeQuery = true)
    List<Quizzes> nonSelectedQuizzes(@Param("topicId") Long topicId,
                                          @Param("ids") List<Long> ids);
}
