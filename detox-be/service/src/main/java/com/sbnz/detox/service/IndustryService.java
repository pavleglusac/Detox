package com.sbnz.detox.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.sbnz.detox.model.*;
import com.sbnz.detox.repository.DiagnosisRepository;
import com.sbnz.detox.repository.DiagnosisResponseRepository;
import com.sbnz.detox.repository.IndustrySymptomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @Autowired
    private DiagnosisResponseRepository diagnosisResponseRepository;

    private final String XSLS_GC_FILE_PATH = "./kjar/src/main/resources/industryGasTemplate.xlsx";
    private final String XSLS_SPECTRO_FILE_PATH = "./kjar/src/main/resources/spectophotometryTemplate.xlsx";

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

        DiagnosisResponse diagnosisResponse = diagnoseSymptoms(symptomsPatched);
        if (diagnosisResponse instanceof DiagnosisResult result) {
            diagnosis.setDiagnosisResult(result);
            diagnosisResponseRepository.save(result);
            diagnosis.setFinished(true);
            diagnosisRepository.save(diagnosis);
        }
        return diagnosisResponse;
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

    public void saveFileGC(MultipartFile file) {
        // save file to resources folder
        String path = XSLS_GC_FILE_PATH;
        try {
            // create file if it doesn't exist
            Files.copy(file.getInputStream(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save file");
        }

    }

    public ResponseEntity<?> getFileGC() {
        // return file from path
        String path = XSLS_GC_FILE_PATH;
        // get input stream and write it to response
        try {
            InputStream inputStream = Files.newInputStream(Paths.get(path));
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=file.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get file");
        }

    }

    public void saveFileSpectro(MultipartFile file) {
        // save file to resources folder
        String path = XSLS_SPECTRO_FILE_PATH;
        try {
            // create file if it doesn't exist
            Files.copy(file.getInputStream(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save file");
        }

    }

    public ResponseEntity<?> getFileSpectro() {
        // return file from path
        String path = XSLS_SPECTRO_FILE_PATH;
        // get input stream and write it to response
        try {
            InputStream inputStream = Files.newInputStream(Paths.get(path));
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=file.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get file");
        }

    }

}
