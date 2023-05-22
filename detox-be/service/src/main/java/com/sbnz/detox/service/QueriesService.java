package com.sbnz.detox.service;

import com.sbnz.detox.exception.UserNotFoundException;
import com.sbnz.detox.model.*;
import com.sbnz.detox.model.queries.Query;
import com.sbnz.detox.model.queries.QueryModel;
import com.sbnz.detox.repository.DiagnosisRepository;
import com.sbnz.detox.repository.PatientRepository;
import com.sbnz.detox.repository.UserRepository;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.api.runtime.rule.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class QueriesService {


    List<QueryModel> model = new ArrayList<>(
            Arrays.asList(
                    new QueryModel("Kokain", "urineTest", "PRESENCE_BENZOILECGONINE"),
                    new QueryModel("Amfetamini", "urineTest", "PRESENCE_AMPHETAMINE"),
                    new QueryModel("urineTest", "AffectsCNS", "false"),
                    new QueryModel("AffectsCNS", "chromatographicImunoassayTest", "DRUGS")
            )
    );

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    public List<String> furtherTestsForUser(String email) throws IllegalAccessException {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Patient with email " + email + " does not exist!"));

        // get latest diagnosis for patient
        Diagnosis diagnosis =  diagnosisRepository.findTopByPatientOrderByStartedAtDesc(patient).orElseThrow(() -> new RuntimeException("Patient with email " + email + " does not have any diagnosis!"));

        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.newKieContainer(ks.newReleaseId("com.sbnz", "kjar", "0.0.1-SNAPSHOT"));
        KieSession kieSession = kieContainer.newKieSession("ksession");
        model.forEach(kieSession::insert);
        
        // check if sympyoms are controlled substances or industry
        if (diagnosis.getSymptoms() instanceof ControlledSubstancesSymptoms) {
            return furtherTests(diagnosis.getSymptoms(), true, kieSession);
        } else {
            return furtherTests(diagnosis.getSymptoms(), false, kieSession);
        }


    }


    public List<String> furtherTests(Symptoms symptoms, boolean controlledSubstances, KieSession kieSession) throws IllegalAccessException {
        // drools rule will return List<String> of further tests
        // keep only the minimum subset for every rule fired (if there are more than one)
        List<String> furtherTests = new ArrayList<>();
        Field[] fields ;
        if (controlledSubstances) {
            fields = ((ControlledSubstancesSymptoms) symptoms).getClass().getDeclaredFields();
        } else {
            fields = ((IndustrySymptoms) symptoms).getClass().getDeclaredFields();
        }

        for (java.lang.reflect.Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(symptoms);
            if(value == null || value.toString().equals("NOT_TESTED")) continue;
            QueryResults results = kieSession.getQueryResults("korisnikKokaina", new Object[]{ Variable.v, field.getName(), value.toString() });
            for (QueryResultsRow row : results) {
                String podvrsta = (String) row.get("podvrstaParam");
                QueryResults resultsSub = kieSession.getQueryResults("korisnikKokainaHelper", new Object[]{ Variable.v, podvrsta });
                for (QueryResultsRow rowSub : resultsSub) {
                    String vrednost = (String) rowSub.get("podvrstaParam");
                    furtherTests.add(vrednost);
                }
            }
        }
        return furtherTests;
    }


    public List<String> getPotentialHeavyDrugsUsers() throws IllegalAccessException {
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.newKieContainer(ks.newReleaseId("com.sbnz", "kjar", "0.0.1-SNAPSHOT"));
        KieSession kieSession = kieContainer.newKieSession("ksession");

        model.forEach(kieSession::insert);
        // get only the last diagnosis for each patient
        HashSet<String> potentialHeavyDrugsUsers = new HashSet<>();
        List<Diagnosis> diagnoses = diagnosisRepository.findLastDiagnosisForPatients();
        for (Diagnosis diagnosis: diagnoses) {
            if (diagnosis.getSymptoms() instanceof ControlledSubstancesSymptoms) {
                ControlledSubstancesSymptoms controlledSubstancesSymptoms = (ControlledSubstancesSymptoms) diagnosis.getSymptoms();
                if (testForPresence("Kokain", controlledSubstancesSymptoms, kieSession) || testForPresence("Amfetamini", controlledSubstancesSymptoms, kieSession)) {
                    potentialHeavyDrugsUsers.add(diagnosis.getPatient().getEmail());
                }
            }
        }
        return potentialHeavyDrugsUsers.stream().toList();
    }


    public boolean testForPresence(String testSubstance, ControlledSubstancesSymptoms controlledSubstancesSymptoms, KieSession kieSession) throws IllegalAccessException {
        boolean allWereTrue = true;

        for (java.lang.reflect.Field field : controlledSubstancesSymptoms.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(controlledSubstancesSymptoms);
            if(value == null || value.toString().equals("NOT_TESTED")) continue;
            Query query = new Query(field.getName(), value.toString(), testSubstance, false);
            kieSession.insert(query);
            kieSession.fireAllRules();
            if (!query.getResult()) {
                allWereTrue = false;
                break;
            }
        }
        return allWereTrue;
    }
}
