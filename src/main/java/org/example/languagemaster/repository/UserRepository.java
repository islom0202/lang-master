package org.example.languagemaster.repository;

import org.example.languagemaster.dto.Ranking;
import org.example.languagemaster.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query(value = "select is_verified from users where email =:userEmail", nativeQuery = true)
    boolean isVerified(@Param("userEmail") String userEmail);

    @Query(value = """
            select sum(score) as total_score, count(id) as total_lessons from user_progress where users_id =:userId
            """, nativeQuery = true)
    Ranking getRanking(@Param("userId") Long userId);
}
