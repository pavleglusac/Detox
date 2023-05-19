package com.sbnz.detox.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.sbnz.detox.model.*;
import com.sbnz.detox.repository.ControlledSubstancesSymptomsRepository;
import com.sbnz.detox.repository.DiagnosisRepository;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Optional;

@Service
public class ControlledSubstancesService {

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @Autowired
    private ControlledSubstancesSymptomsRepository controlledSubstancesSymptomsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KieContainer kieContainer;


    public Object patchControlledSubstanceSymptom(Long diagnosisId, JsonPatch patch) {
        Diagnosis diagnosis = diagnosisRepository
                                .findById(diagnosisId)
                                .orElseThrow(() -> new RuntimeException("Diagnosis not found"));

        // get the symptoms from the diagnosis
        Optional<ControlledSubstancesSymptoms> symptoms = controlledSubstancesSymptomsRepository
                                                            .findById(diagnosis.getSymptoms().getId());

        if (symptoms.isEmpty()) {
            throw new RuntimeException("Symptoms not found");
        }

        ControlledSubstancesSymptoms symptoms1 = symptoms.get();
        ControlledSubstancesSymptoms symptomsPatched = applyPatchToControlledSubstancesSymptoms(patch, symptoms1);
        symptomsPatched.setId(symptoms1.getId());
        diagnosis.setSymptoms(symptomsPatched);
        diagnosisRepository.save(diagnosis);
        controlledSubstancesSymptomsRepository.save(symptomsPatched);

        return diagnoseSymptoms(symptomsPatched);
    }

    private Object diagnoseSymptoms(ControlledSubstancesSymptoms symptomsPatched) {
        // create a new session and insert the diagnosis
        System.out.println("Diagnosing symptoms");
        KieSession kieSession = kieContainer.newKieSession("ksession");
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

    private ControlledSubstancesSymptoms applyPatchToControlledSubstancesSymptoms(JsonPatch patch, ControlledSubstancesSymptoms target) {
        // print json representation
        System.out.println(objectMapper.convertValue(target, JsonNode.class));
        try {
            JsonNode patched = patch.apply(objectMapper.convertValue(target, JsonNode.class));
            return objectMapper.treeToValue(patched, ControlledSubstancesSymptoms.class);
        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Failed to apply patch to controlled substances symptoms object");
        }
    }
}
