package org.example.languagemaster.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyGroups implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column private String title;
  @Column private LocalDateTime createdAt;
  @Column private LocalDateTime updatedAt;
}
