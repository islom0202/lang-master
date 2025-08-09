package org.example.languagemaster.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table
@Data
public class VocabularyGroups implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "level_id")
  private Levels levels;
  @Column private String title;
  @Column private LocalDateTime createdAt;
  @Column private LocalDateTime updatedAt;
}
