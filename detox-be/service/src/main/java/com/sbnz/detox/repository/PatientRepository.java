package com.sbnz.detox.repository;

import com.sbnz.detox.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    public Optional<Patient> findByEmail(String email);
}
