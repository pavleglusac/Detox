package com.sbnz.detox.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Patient patient;

    @OneToOne
    private Symptoms symptoms;

    private Boolean finished = false;

    private LocalDateTime startedAt = LocalDateTime.now();

    @OneToOne
    private DiagnosisResult diagnosisResult;
}
