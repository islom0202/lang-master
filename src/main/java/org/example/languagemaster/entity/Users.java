package org.example.languagemaster.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.languagemaster.entity.enums.UserRole;
import org.mapstruct.control.DeepClone;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column private Long id;
  @Column private String firstname;
  @Column private String lastname;
  @Column private String password;
  @Column private String email;
  @Enumerated(EnumType.STRING)
  @Column
  private UserRole role;
  @JoinColumn
  @ManyToOne
  private Levels langLevel;
  @Column private boolean isVerified;
  @Column private LocalDateTime createdAt;
  @Column private LocalDateTime updatedAt;
}
