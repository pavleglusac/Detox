package com.sbnz.detox.repository;

import com.sbnz.detox.model.ControlledSubstancesSymptoms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlledSubstancesSymptomsRepository extends JpaRepository<ControlledSubstancesSymptoms, Long> {
}
