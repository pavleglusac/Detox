package com.sbnz.detox.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.sbnz.detox.model.*;
import com.sbnz.detox.repository.DiagnosisRepository;
import com.sbnz.detox.repository.IndustrySymptomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.Optional;

@Service
public class IndustryService {

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @Autowired
    private IndustrySymptomsRepository industrySymptomsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KieContainer kieContainer;

    public DiagnosisResponse patchIndustrySymptom(Long diagnosisId, JsonPatch patch) {
        Diagnosis diagnosis = diagnosisRepository
                .findById(diagnosisId)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found"));

        // get the symptoms from the diagnosis
        Optional<IndustrySymptoms> symptoms = industrySymptomsRepository
                .findById(diagnosis.getSymptoms().getId());

        if (symptoms.isEmpty()) {
            throw new RuntimeException("Symptoms not found");
        }

        IndustrySymptoms symptoms1 = symptoms.get();
        IndustrySymptoms symptomsPatched = applyPatchToIndustrySymptoms(patch, symptoms1);
        symptomsPatched.setId(symptoms1.getId());
        diagnosis.setSymptoms(symptomsPatched);
        diagnosisRepository.save(diagnosis);
        industrySymptomsRepository.save(symptomsPatched);

        return diagnoseSymptoms(symptomsPatched);
    }

    public DiagnosisResponse diagnoseSymptoms(IndustrySymptoms symptomsPatched) {
        // create a new session and insert the diagnosis
        System.out.println("Diagnosing symptoms");
        KieSession kieSession = kieContainer.newKieSession("ksession-industry");
        kieSession.insert(symptomsPatched);
        kieSession.fireAllRules();
        System.out.println("Diagnosis done");
        // extract object from session
        Collection<Question> questionList = (Collection<Question>) kieSession.getObjects(new ClassObjectFilter(Question.class));
        if (questionList.isEmpty()) {
            Collection<DiagnosisResult> diagnosisResultList = (Collection<DiagnosisResult>) kieSession.getObjects(new ClassObjectFilter(DiagnosisResult.class));
            if (diagnosisResultList.isEmpty()) {
                throw new RuntimeException("Diagnosis result not found");
            }
            kieSession.dispose();
            if (diagnosisResultList.stream().toList().size() > 1) {
                throw new RuntimeException("More than one diagnosis result found");
            }
            diagnosisResultList.stream().toList().get(0).setDiagnosisId(symptomsPatched.getId());
            return diagnosisResultList.stream().toList().get(0);
        }
        kieSession.dispose();
        if (questionList.stream().toList().size() > 1) {
            throw new RuntimeException("More than one question found");
        }
        questionList.stream().toList().get(0).setDiagnosisId(symptomsPatched.getId());
        return questionList.stream().toList().get(0);
    }

    private IndustrySymptoms applyPatchToIndustrySymptoms(JsonPatch patch, IndustrySymptoms target) {
        // print json representation
        System.out.println(objectMapper.convertValue(target, JsonNode.class));
        try {
            JsonNode patched = patch.apply(objectMapper.convertValue(target, JsonNode.class));
            return objectMapper.treeToValue(patched, IndustrySymptoms.class);
        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Failed to apply patch to industry substances symptoms object");
        }
    }
}
