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
	public DrugsParams drugsParams() {
		DrugsParams drugsParams = new DrugsParams();
		drugsParams.setCocaineConcentration(80.0);
		drugsParams.setCocaineTime(1);
		drugsParams.setOpioidConcentration(60.0);
		drugsParams.setOpioidTime(2);
		drugsParams.setMethamphetamineConcentration(100.0);
		drugsParams.setMethamphetamineTime(3);
		drugsParams.setBenzodiazepineConcentration(30.0);
		drugsParams.setBenzodiazepineTime(4);
		drugsParams.setSyntheticCannabinoidConcentration(50.0);
		drugsParams.setSyntheticCannabinoidTime(0);
		return drugsParams;
	}

}
