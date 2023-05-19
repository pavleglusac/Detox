package com.sbnz.detox.repository;

import com.sbnz.detox.model.IndustrySymptoms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustrySymptomsRepository extends JpaRepository<IndustrySymptoms, Long> {
}
