package com.sbnz.detox;

import com.sbnz.detox.config.AppProperties;
import com.sbnz.detox.model.gas_chromatography_drugs.DrugsParams;
import jakarta.annotation.PostConstruct;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.security.Security;
import java.util.List;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties(AppProperties.class)
public class DetoxApplication {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(DetoxApplication.class, args);
	}

	@PostConstruct
	public void init() {
		Security.addProvider(new BouncyCastleProvider());

	}

	@Bean
	public KieContainer kieContainer() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("com.sbnz", "kjar", "0.0.1-SNAPSHOT"));
		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(1000);
		return kContainer;
	}


	@Bean
	public List<DrugsParams> drugsParams() {
		DrugsParams drugsParams1 = new DrugsParams();
		drugsParams1.setParamsName("HIGH");
		drugsParams1.setCocaineConcentrationMin(80.0);
		drugsParams1.setCocaineConcentrationMax(200.0);
		drugsParams1.setCocaineTime(1);
		drugsParams1.setOpioidConcentrationMin(60.0);
		drugsParams1.setOpioidConcentrationMin(200.0);
		drugsParams1.setOpioidTime(2);
		drugsParams1.setMethamphetamineConcentrationMin(100.0);
		drugsParams1.setMethamphetamineConcentrationMin(300.0);
		drugsParams1.setMethamphetamineTime(3);
		drugsParams1.setBenzodiazepineConcentrationMin(30.0);
		drugsParams1.setBenzodiazepineConcentrationMax(150.0);
		drugsParams1.setBenzodiazepineTime(4);
		drugsParams1.setSyntheticCannabinoidConcentrationMin(50.0);
		drugsParams1.setSyntheticCannabinoidConcentrationMax(250.0);
		drugsParams1.setSyntheticCannabinoidTime(0);

		DrugsParams drugsParams2 = new DrugsParams();
		drugsParams2.setParamsName("LOW");
		drugsParams2.setCocaineConcentrationMin(40.0);
		drugsParams2.setCocaineConcentrationMax(80.0);
		drugsParams2.setCocaineTime(1);
		drugsParams2.setOpioidConcentrationMin(30.0);
		drugsParams2.setOpioidConcentrationMin(60.0);
		drugsParams2.setOpioidTime(2);
		drugsParams2.setMethamphetamineConcentrationMin(50.0);
		drugsParams2.setMethamphetamineConcentrationMin(100.0);
		drugsParams2.setMethamphetamineTime(3);
		drugsParams2.setBenzodiazepineConcentrationMin(20.0);
		drugsParams2.setBenzodiazepineConcentrationMax(30.0);
		drugsParams2.setBenzodiazepineTime(4);
		drugsParams2.setSyntheticCannabinoidConcentrationMin(30.0);
		drugsParams2.setSyntheticCannabinoidConcentrationMax(50.0);
		drugsParams2.setSyntheticCannabinoidTime(0);

		return List.of(drugsParams1, drugsParams2);
	}

}
