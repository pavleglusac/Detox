package com.sbnz.detox.repository;

import com.sbnz.detox.model.Diagnosis;
import com.sbnz.detox.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Diagnosis d WHERE d.patient = ?1 AND d.finished = false")
    public Boolean hasDiagnosisInProgress(Patient patient);

    @Query("SELECT d FROM Diagnosis d WHERE d.patient = ?1 AND d.finished = false")
    Optional<Diagnosis> findDiagnosisInProgress(Patient patient);

    List<Diagnosis> findAllByPatient(Patient patient);

    Optional<Diagnosis> findTopByPatientOrderByStartedAtDesc(Patient patient);


    // get the last diagnosis for every patient, only one diagnosis per patient
    @Query("SELECT MAX(d.startedAt) FROM Diagnosis d GROUP BY d.patient.id")
    List<Diagnosis> findLastDiagnosisForPatients();
}
