package org.example.languagemaster.repository;

import org.example.languagemaster.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
  @Query(
      value =
          "select section_id from user_progress where users_id =:userId and section_type =:sectionType order by section_id",
      nativeQuery = true)
  List<Long> endedTopicsId(@Param("userId") Long userId, @Param("sectionType") String sectionType);

  List<UserProgress> findAllByUsers_Id(Long users);

  @Query(
      value =
          """
    select * from user_progress where users_id =:userId and section_type = 'GAME' and section_id in (:ids)
    """,
      nativeQuery = true)
  List<UserProgress> findAllByUserAndGameIds(
      @Param("userId") Long userId, @Param("ids") Set<Long> ids);
}
