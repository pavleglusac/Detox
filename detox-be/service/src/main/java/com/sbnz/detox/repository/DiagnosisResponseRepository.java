package com.sbnz.detox.repository;

import com.sbnz.detox.model.DiagnosisResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosisResponseRepository extends JpaRepository<DiagnosisResponse, Long> {
}
