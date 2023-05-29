package com.sbnz.detox.controller;

import com.sbnz.detox.model.Diagnosis;
import com.sbnz.detox.model.DiagnosisResponse;
import com.sbnz.detox.service.DiagnosisService;
import jdk.jshell.Diag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/diagnosis")
public class DiagnosisController {

    @Autowired
    private DiagnosisService diagnosisService;


    @PostMapping("/start")
    public Long startDiagnosis(@RequestParam("userEmail") String email) {
        return diagnosisService.startDiagnosis(email);
    }

    @GetMapping("")
    public Diagnosis getCurrentDiagnosis(@RequestParam("userEmail") String email) {
        return diagnosisService.getCurrentDiagnosis(email);
    }

    @GetMapping("/all")
    public Iterable<Diagnosis> getAllDiagnosis(@RequestParam(value = "userEmail", required = false) Optional<String> email) {
        if (email.isPresent()) {
            return diagnosisService.getAllDiagnosis(email.get());
        }
        return diagnosisService.getAllDiagnosis();
    }

    @PatchMapping("/set-symptoms")
    public DiagnosisResponse setSymptoms(@RequestParam("diagnosisId") Long diagnosisId, @RequestParam("symptomsType") String symptomsType) {
        return diagnosisService.setSymptomsType(diagnosisId, symptomsType);
    }

    @PatchMapping("/reset-symptoms")
    public void resetSymptoms(@RequestParam("diagnosisId") Long diagnosisId) {
        diagnosisService.resetSymptoms(diagnosisId);
    }

    @PatchMapping("/end-diagnosis")
    public void endDiagnosis(@RequestParam("diagnosisId") Long diagnosisId) {
        diagnosisService.endDiagnosis(diagnosisId);
    }

}
