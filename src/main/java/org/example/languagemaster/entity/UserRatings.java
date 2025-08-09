package org.example.languagemaster.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
@Data
public class UserRatings implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users users;
    @Column
    private int total_score;
    @Column
    private int rankingPosition;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
}
