package com.sbnz.detox.service;

import com.sbnz.detox.exception.UserNotFoundException;
import com.sbnz.detox.model.*;
import com.sbnz.detox.model.queries.Query;
import com.sbnz.detox.model.queries.QueryModel;
import com.sbnz.detox.repository.DiagnosisRepository;
import com.sbnz.detox.repository.PatientRepository;
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


    List<QueryModel> drugsModel = new ArrayList<>(
            Arrays.asList(
                    new QueryModel("Kokain", "urineTest", "PRESENCE_BENZOILECGONINE"),
                    new QueryModel("Marihuana", "urineTest", "PRESENCE_11_HYDROXY_9_TETRAHYDROCANNABINOL"),
                    new QueryModel("Amfetamini", "urineTest", "PRESENCE_AMPHETAMINE"),
                    new QueryModel("urineTest", "CNSDepression", "false"),
                    new QueryModel("CNSDepression", "chromatographicImunoassayTest", "DRUGS"),
                    new QueryModel("ReactsToKVS", "chromatographicImunoassayTest", "MEDICINE"),

                    //
                    new QueryModel("LocksonTest", "CNSDepression", "true"),
                    new QueryModel("Morfin", "LocksonTest", "true"),
                    new QueryModel("TLCAcidicTest", "LocksonTest", "false"),
                    new QueryModel("Gasna hromatografija", "TLCAcidicTest", "false"),
                    new QueryModel("Convulsions", "TLCAcidicTest", "true"),
                    new QueryModel("Kodein", "Convulsions", "false"),
                    new QueryModel("Tramadol", "Convulsions", "true"),


                    //
                    new QueryModel("Penicilin", "HasArds", "false"),
                    new QueryModel("Bleomicin", "HasArds", "true"),
                    new QueryModel("HasArds", "HasLungFibrosis", "false"),
                    new QueryModel("Ciklosporin", "HasLungFibrosis", "true"),
                    new QueryModel("HasLungFibrosis", "AffectsCNS", "false"),

                    new QueryModel("Benzodiazepini", "InDeepComa", "false"),
                    new QueryModel("Barbiturati", "InDeepComa", "true"),
                    new QueryModel("InDeepComa", "AffectsCNS", "true"),

                    new QueryModel("AffectsCNS", "ReactsToKVS", "false"),
                    new QueryModel("TLCBasicTest", "ReactsToKVS", "true"),
                    new QueryModel("AcidityTest", "TLCBasicTest", "false"),
                    new QueryModel("Beta blokatori", "TLCBasicTest", "false"),
                    new QueryModel("ACE inhibitori", "AcidityTest", "false"),
                    new QueryModel("Salicilati", "AcidityTest", "false")



            )
    );


    List<QueryModel> industryModel = new ArrayList<>(
            Arrays.asList(
                    new QueryModel("Azbest", "pulmonaryEdema", "false"),
                    new QueryModel("Ozon", "pulmonaryEdema", "true"),
                    new QueryModel("Vodonik-sulfid", "epaTest", "true"),
                    new QueryModel("pulmonaryEdema", "epaTest", "false"),
                    new QueryModel("epaTest", "damageToTheRespiratoryTract", "LOWER"),
                    new QueryModel("Gasna hromatografija", "ffpTest", "false"),
                    new QueryModel("Sumpor dioksid", "ffpTest", "true"),
                    new QueryModel("Gasna hromatografija", "ormTest", "false"),
                    new QueryModel("ffpTest", "ormTest", "true"),
                    new QueryModel("ormTest", "damageToTheRespiratoryTract", "UPPER"),
                    new QueryModel("damageToTheRespiratoryTract", "showsMethemoglobinemia", "false"),
                    new QueryModel("giTest", "showsMethemoglobinemia", "true"),
                    new QueryModel("Azotovi oksidi", "giTest", "true"),
                    new QueryModel("amilNitriteTest", "giTest", "false"),
                    new QueryModel("Cijanidi", "amilNitriteTest", "true"),
                    new QueryModel("Spektrofotometrija", "amilNitriteTest", "false"),
                    new QueryModel("showsMethemoglobinemia", "worksWithToxicGases", "true"),
                    new QueryModel("cns", "worksWithToxicGases", "false"),
                    new QueryModel("potassiumDichromateTest", "cns", "true"),
                    new QueryModel("neurotoxicity", "cns", "false"),
                    new QueryModel("Alkoholna supstanca", "potassiumDichromateTest", "true"),
                    new QueryModel("Gasna hromatografija", "potassiumDichromateTest", "false"),
                    new QueryModel("Gasna hromatografija", "neurotoxicity", "false"),
                    new QueryModel("Ugljen disulfid", "neurotoxicity", "true")
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

        // check if sympyoms are controlled substances or industry
        if (diagnosis.getSymptoms() instanceof ControlledSubstancesSymptoms) {
            drugsModel.forEach(kieSession::insert);
            return furtherTests(diagnosis.getSymptoms(), true, kieSession);
        } else {
            industryModel.forEach(kieSession::insert);
            return furtherTests(diagnosis.getSymptoms(), false, kieSession);
        }
    }


    public List<String> furtherTests(Symptoms symptoms, boolean controlledSubstances, KieSession kieSession) throws IllegalAccessException {
        // drools rule will return List<String> of further tests
        // keep only the minimum subset for every rule fired (if there are more than one)
        List<String> furtherTests = new ArrayList<>();
        HashSet<String> tempTests = new HashSet<>();
        Field[] fields;
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
                tempTests.add(podvrsta);
                QueryResults resultsSub = kieSession.getQueryResults("korisnikKokainaHelper", new Object[]{ Variable.v, podvrsta });
                for (QueryResultsRow rowSub : resultsSub) {
                    String vrednost = (String) rowSub.get("podvrstaParam");
                    tempTests.add(vrednost);
                }
                if (tempTests.size() < furtherTests.size() || furtherTests.isEmpty()) {
                    furtherTests.clear();
                    furtherTests.addAll(tempTests);
                }
                tempTests.clear();
            }
        }
        return furtherTests;
    }


    public List<String> getPotentialHeavyDrugsUsers() throws IllegalAccessException {
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.newKieContainer(ks.newReleaseId("com.sbnz", "kjar", "0.0.1-SNAPSHOT"));
        KieSession kieSession = kieContainer.newKieSession("ksession");

        drugsModel.forEach(kieSession::insert);
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


    public boolean testForPresence(String testSubstance, Symptoms symptoms, KieSession kieSession) throws IllegalAccessException {
        boolean allWereTrue = true;
        Class<?> currentClass = symptoms.getClass();
        for (java.lang.reflect.Field field : currentClass.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(symptoms);
            if(value == null || value.toString().equals("NOT_TESTED") || value.toString().equals("NONE")) continue;
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



    public List<String> getPotentialLifeEndangered() throws IllegalAccessException {
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.newKieContainer(ks.newReleaseId("com.sbnz", "kjar", "0.0.1-SNAPSHOT"));
        KieSession kieSession = kieContainer.newKieSession("ksession");

        industryModel.forEach(kieSession::insert);
        // get only the last diagnosis for each patient
        HashSet<String> potentialEndangeredPatients = new HashSet<>();
        List<Diagnosis> diagnoses = diagnosisRepository.findLastDiagnosisForPatients();
        for (Diagnosis diagnosis: diagnoses) {
            if (diagnosis.getSymptoms() instanceof IndustrySymptoms industrySymptoms) {
                if (testForPresence("Ugljen disulfid", industrySymptoms, kieSession) || testForPresence("Cijanidi", industrySymptoms, kieSession)) {
                    potentialEndangeredPatients.add(diagnosis.getPatient().getEmail());
                }
            }
        }
        return potentialEndangeredPatients.stream().toList();
    }



    public List<String> testsNeededForToxin(String toxin) {
        System.out.println("Uso");
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.newKieContainer(ks.newReleaseId("com.sbnz", "kjar", "0.0.1-SNAPSHOT"));
        KieSession kieSession = kieContainer.newKieSession("ksession");

        HashSet<String> result = new HashSet<>();

        drugsModel.forEach(kieSession::insert);

        QueryResults results = kieSession.getQueryResults("korisnikKokainaHelper", new Object[]{ toxin, Variable.v });
        for (QueryResultsRow row : results) {
            String vrstaParam = (String) row.get("vrstaParam");
            result.add(vrstaParam);
        }

        // clear kie session
        kieSession.dispose();
        kieSession = kieContainer.newKieSession("ksession");
        industryModel.forEach(kieSession::insert);
        results = kieSession.getQueryResults("korisnikKokainaHelper", new Object[]{ toxin, Variable.v });
        for (QueryResultsRow row : results) {
            String vrstaParam = (String) row.get("vrstaParam");
            result.add(vrstaParam);
        }
        return result.stream().toList();
    }
}
