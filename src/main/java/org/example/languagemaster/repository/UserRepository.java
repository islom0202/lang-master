package org.example.languagemaster.repository;

import org.example.languagemaster.dto.Ranking;
import org.example.languagemaster.dto.UserRankingRes;
import org.example.languagemaster.dto.UserRankingResDtop;
import org.example.languagemaster.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
  Optional<Users> findByEmail(String email);

  boolean existsByEmail(String email);

  @Query(value = "select is_verified from users where email =:userEmail", nativeQuery = true)
  boolean isVerified(@Param("userEmail") String userEmail);

  @Query(
      value =
          """
            select sum(score) as total_score, count(id) as total_lessons from user_progress where users_id =:userId
            """,
      nativeQuery = true)
  Ranking getRanking(@Param("userId") Long userId);

  @Query(value = """
    SELECT u.id as user_id, 
            u.firstname AS firstname,
           u.lastname AS lastname,
           ll.level AS level,
           SUM(up.score) AS total_score,
           COUNT(up.id) AS lessons
    FROM users u
    LEFT JOIN user_progress up ON u.id = up.users_id
    LEFT JOIN levels ll ON u.lang_level_id = ll.id
    WHERE up.completed_at BETWEEN :begin AND :end
    GROUP BY u.id, ll.level
    """,
          countQuery = """
    SELECT COUNT(DISTINCT u.id)
    FROM users u
    LEFT JOIN user_progress up ON u.id = up.users_id
    WHERE up.completed_at BETWEEN :begin AND :end
    """,
          nativeQuery = true)
  Page<UserRankingResDtop> allRanking(@Param("begin") LocalDateTime begin,
                                      @Param("end") LocalDateTime end,
                                      Pageable pageable);


}
