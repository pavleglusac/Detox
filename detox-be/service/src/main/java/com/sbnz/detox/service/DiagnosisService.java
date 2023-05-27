package com.sbnz.detox.service;

import com.sbnz.detox.exception.UserNotFoundException;
import com.sbnz.detox.model.*;
import com.sbnz.detox.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiagnosisService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @Autowired
    private ControlledSubstancesSymptomsRepository controlledSubstancesSymptomsRepository;

    @Autowired
    private IndustrySymptomsRepository industrySymptomsRepository;

    @Autowired
    private ControlledSubstancesService controlledSubstancesService;

    @Autowired
    private IndustryService industryService;

    public Long startDiagnosis(String email) {

        Patient patient = patientRepository
                            .findByEmail(email)
                            .orElseThrow(() -> new UserNotFoundException("User not found"));

        Boolean hasDiagnosisInProgress = diagnosisRepository.hasDiagnosisInProgress(patient);

        if (hasDiagnosisInProgress) {
            throw new RuntimeException("Diagnosis already in progress");
        }

        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPatient(patient);
        diagnosisRepository.save(diagnosis);
        return diagnosis.getId();
    }

    public Diagnosis getCurrentDiagnosis(String email) {

            Patient patient = patientRepository
                                .findByEmail(email)
                                .orElseThrow(() -> new UserNotFoundException("User not found"));

            Diagnosis diagnosis = diagnosisRepository
                                .findDiagnosisInProgress(patient)
                                .orElseThrow(() -> new RuntimeException("Diagnosis not found"));

            return diagnosis;
    }


    public List<Diagnosis> getAllDiagnosis(String email) {
        Patient patient = patientRepository
                            .findByEmail(email)
                            .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Diagnosis> diagnosis = diagnosisRepository
                                    .findAllByPatient(patient);
        return diagnosis;
    }

    public List<Diagnosis> getAllDiagnosis() {
        List<Diagnosis> diagnosis = diagnosisRepository
                                    .findAll();
        return diagnosis;
    }


    public DiagnosisResponse setSymptomsType(Long diagnosisId, String symptomsType) {

        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId).orElseThrow(() -> new RuntimeException("Diagnosis not found"));
        if (diagnosis.getSymptoms() != null) {
            throw new RuntimeException("Symptoms already set");
        }
        Symptoms symptoms = null;
        DiagnosisResponse diagnosisResponse = null;
        if (symptomsType.equals("INDUSTRY")) {
            symptoms = new IndustrySymptoms();
            industrySymptomsRepository.save((IndustrySymptoms) symptoms);
            diagnosisResponse = this.industryService.diagnoseSymptoms((IndustrySymptoms) symptoms);
        } else if (symptomsType.equals("CONTROLLED_SUBSTANCES")) {
            symptoms = new ControlledSubstancesSymptoms();
            controlledSubstancesSymptomsRepository.save((ControlledSubstancesSymptoms) symptoms);
            diagnosisResponse = this.controlledSubstancesService.diagnoseSymptoms((ControlledSubstancesSymptoms) symptoms);
        } else {
            throw new RuntimeException("Invalid symptoms type");
        }
        diagnosis.setSymptoms(symptoms);
        diagnosisRepository.save(diagnosis);
        return diagnosisResponse;
    }

    public void setDiagnosisResult(Long diagnosisId, DiagnosisResult result) {
        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId).orElseThrow(() -> new RuntimeException("Diagnosis not found"));
        diagnosis.setDiagnosisResult(result);
        diagnosis.setFinished(true);
        diagnosisRepository.save(diagnosis);
    }

    public void resetSymptoms(Long diagnosisId) {
        // set symptoms to null
        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId).orElseThrow(() -> new RuntimeException("Diagnosis not found"));
        diagnosis.setSymptoms(null);
        diagnosisRepository.save(diagnosis);
        // set diagnosis result to null
        diagnosis.setDiagnosisResult(null);
        diagnosisRepository.save(diagnosis);
    }

    public void endDiagnosis(Long diagnosisId) {
        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId).orElseThrow(() -> new RuntimeException("Diagnosis not found"));
        diagnosis.setFinished(true);
        DiagnosisResult diagnosisResult = new DiagnosisResult();
        diagnosisResult.setContent("NOT FINISHED");
        diagnosisRepository.save(diagnosis);
    }
}
