package org.example.languagemaster.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Table
@Data
public class Games implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String gameType;

    @Column
    private String word;

    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> options;

    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> correctAnswers;
}
