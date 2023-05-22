package com.sbnz.detox;

import com.sbnz.detox.model.ChromatographicImunoassayTestResult;
import com.sbnz.detox.model.ControlledSubstancesSymptoms;
import com.sbnz.detox.model.DiagnosisResult;
import com.sbnz.detox.model.UrineTestResult;
import com.sbnz.detox.model.gas_chromatography_drugs.DrugsParams;
import com.sbnz.detox.model.queries.Query;
import com.sbnz.detox.model.queries.QueryModel;
import com.sbnz.detox.service.GasChromatographyDrugsService;
import org.drools.core.QueryResultsRowImpl;
import org.drools.template.ObjectDataCompiler;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.api.runtime.rule.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.*;

class AdminApplicationTests {

//	@Autowired
//	private GasChromatographyDrugsService gasChromatographyDrugsService;

	List<QueryModel> model = new ArrayList<>(
			Arrays.asList(
					new QueryModel("Kokain", "urineTest", "PRESENCE_BENZOILECGONINE"),
					new QueryModel("Amfetamini", "urineTest", "PRESENCE_AMPHETAMINE"),
					new QueryModel("urineTest", "AffectsCNS", "false"),
					new QueryModel("AffectsCNS", "chromatographicImunoassayTest", "DRUGS")
			)
	);

	@Test
	void contextLoads() throws IOException, IllegalAccessException, InstantiationException {
		KieServices ks = KieServices.Factory.get();
		KieContainer kieContainer = ks.newKieContainer(ks.newReleaseId("com.sbnz", "kjar", "0.0.1-SNAPSHOT"));
		KieSession kieSession = kieContainer.newKieSession("ksession");

		model.forEach(kieSession::insert);

		ControlledSubstancesSymptoms controlledSubstancesSymptoms = new ControlledSubstancesSymptoms();
		controlledSubstancesSymptoms.setAffectsCNS(false);
		controlledSubstancesSymptoms.setUrineTest(UrineTestResult.PRESENCE_BENZOILECGONINE);
		// using reflection, get all fields of ControlledSubstanceSymptoms class
		// and get the value of each field
		assert testForPresence("Kokain", controlledSubstancesSymptoms, kieSession);


		assert !testForPresence("Marihuana", controlledSubstancesSymptoms, kieSession);

		new ControlledSubstancesSymptoms();
		controlledSubstancesSymptoms.setChromatographicImunoassayTest(ChromatographicImunoassayTestResult.DRUGS);
		controlledSubstancesSymptoms.setAffectsCNS(false);
		controlledSubstancesSymptoms.setUrineTest(UrineTestResult.PRESENCE_11_HYDROXY_9_TETRAHYDROCANNABINOL);
		assert !testForPresence("Kokain", controlledSubstancesSymptoms, kieSession);
		assert !testForPresence("Amfetamini", controlledSubstancesSymptoms, kieSession);

		controlledSubstancesSymptoms.setUrineTest(UrineTestResult.PRESENCE_AMPHETAMINE);
		assert !testForPresence("Kokain", controlledSubstancesSymptoms, kieSession);
		assert testForPresence("Amfetamini", controlledSubstancesSymptoms, kieSession);

		controlledSubstancesSymptoms.setChromatographicImunoassayTest(ChromatographicImunoassayTestResult.MEDICINE);
		assert !testForPresence("Kokain", controlledSubstancesSymptoms, kieSession);
		assert !testForPresence("Amfetamini", controlledSubstancesSymptoms, kieSession);

	}


	@Test
	public void testFurtherTests() throws IllegalAccessException {
		KieServices ks = KieServices.Factory.get();
		KieContainer kieContainer = ks.newKieContainer(ks.newReleaseId("com.sbnz", "kjar", "0.0.1-SNAPSHOT"));
		KieSession kieSession = kieContainer.newKieSession("ksession");
		model.forEach(kieSession::insert);

		ControlledSubstancesSymptoms controlledSubstancesSymptoms = new ControlledSubstancesSymptoms();
		controlledSubstancesSymptoms.setAffectsCNS(false);
		controlledSubstancesSymptoms.setUrineTest(UrineTestResult.PRESENCE_BENZOILECGONINE);

		furtherTests("Kokain", controlledSubstancesSymptoms, kieSession).forEach(System.out::println);
	}


	public List<String> furtherTests(String testSubstance, ControlledSubstancesSymptoms controlledSubstancesSymptoms, KieSession kieSession) throws IllegalAccessException {
		// drools rule will return List<String> of further tests
		// keep only the minimum subset for every rule fired (if there are more than one)
		System.out.println("ODAVDE DOLE ............................................");
		List<String> furtherTests = new ArrayList<>();
		HashSet<String> tempList = new HashSet<>();
		for (java.lang.reflect.Field field : controlledSubstancesSymptoms.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object value = field.get(controlledSubstancesSymptoms);
			if(value == null || value.toString().equals("NOT_TESTED")) continue;
			System.out.println("Query za " + field.getName() + " je " + value.toString());
//			String query = field.getName();
//			kieSession.insert(query);
			QueryResults results = kieSession.getQueryResults("korisnikKokaina", new Object[]{ Variable.v, field.getName(), value.toString() });
			// get all results
			for (QueryResultsRow row : results) {
				// get the result
				String podvrsta = (String) row.get("podvrstaParam");
				QueryResults resultsSub = kieSession.getQueryResults("korisnikKokainaHelper", new Object[]{ Variable.v, podvrsta });
				System.out.println("Result podvrsta (vrednost ukljucena): " + podvrsta);
				for (QueryResultsRow rowSub : resultsSub) {
					String vrednost = (String) rowSub.get("podvrstaParam");
					System.out.println("Result: " + vrednost);
					tempList.add(vrednost);
				}
				// keep the shorter list
				if(furtherTests.isEmpty() || furtherTests.size() > tempList.size()) {
					furtherTests.clear();
					furtherTests.addAll(tempList);
				}
				tempList.clear();
//				furtherTests.add(v.getValue());
			}
		}
		return furtherTests;
	}


	public boolean testForPresence(String testSubstance, ControlledSubstancesSymptoms controlledSubstancesSymptoms, KieSession kieSession) throws IllegalAccessException {
		boolean allWereTrue = true;

		for (java.lang.reflect.Field field : controlledSubstancesSymptoms.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object value = field.get(controlledSubstancesSymptoms);
			if(value == null || value.toString().equals("NOT_TESTED")) continue;
			System.out.println("Query za " + field.getName() + " je " + value.toString());
			Query query = new Query(field.getName(), value.toString(), testSubstance, false);
			kieSession.insert(query);
			kieSession.fireAllRules();
			if (!query.getResult()) {
				allWereTrue = false;
				break;
			}
		}

		if(allWereTrue) {
			System.out.println("!!! Detektovana je supstanca !!!");
		} else {
			System.out.println("Nista nije detektovano");
		}
		return allWereTrue;
	}


}
