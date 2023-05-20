package com.sbnz.detox.service;

import com.sbnz.detox.DetoxApplication;
import com.sbnz.detox.model.DiagnosisResult;
import com.sbnz.detox.model.gas_chromatography_drugs.*;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class GasChromatographyDrugsService {


    @Autowired
    private KieContainer kieContainer;

    @Autowired
    private DrugsParams drugsParams;



    class DrugGenerator<T extends DrugSample> {
        private final Class<T> clazz;
        private final int minValue;
        private final int maxValue;
        private final Random random = new Random();

        private final Double skipChances;

        public DrugGenerator(Class<T> clazz, int minValue, int maxValue, Double skipChances) {
            this.clazz = clazz;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.skipChances = skipChances;
        }

        public T generateDrug() throws IllegalAccessException, InstantiationException {
            T drug = clazz.newInstance();
            drug.setConcentration(generateConcentration());
            drug.setTimestamp(new Date());
            return drug;
        }

        public double generateConcentration() {
            return minValue + (maxValue - minValue) * random.nextDouble();
        }

        public boolean skip() {
            return random.nextDouble() < skipChances;
        }
    }

    List<DrugGenerator> generators = List.of(
            new DrugGenerator<>(Cocaine.class, 0, 50, 0.1),
            new DrugGenerator<>(Opioid.class, 0, 80, 0.4),
            new DrugGenerator<>(Methamphetamine.class, 0, 120, 0.4),
            new DrugGenerator<>(Benzodiazepine.class, 0, 120, 0.4),
            new DrugGenerator<>(SyntheticCannabinoid.class, 0, 100, 0.0)
    );

    public DiagnosisResult runGasChromatography() throws IllegalAccessException, InstantiationException, FileNotFoundException {
        File file = new File("./kjar/src/main/resources/rules/controlled_substances/gas_chromatography_substances.drt");

        InputStream template = new FileInputStream(file);
        List<DrugsParams> tmpList = new ArrayList<>();
        tmpList.add(drugsParams);

        ObjectDataCompiler converter = new ObjectDataCompiler();
        String drl = converter.compile(tmpList, template);
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.write("src/main/resources/rules.drl", kieServices.getResources().newReaderResource(new StringReader(drl)));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs);
        kieBuilder.buildAll();
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kieBuilder.getResults().toString());
        }

        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        KieBaseConfiguration kieBaseConf = kieServices.newKieBaseConfiguration();
        kieBaseConf.setOption(EventProcessingOption.STREAM);
        KieBase kieBase = kieContainer.newKieBase(kieBaseConf);
        KieSession kieSession = kieBase.newKieSession();

        while(true) {
            for (DrugGenerator generator : generators) {
                if (generator.skip()) {
                    continue;
                }
                DrugSample drugSample = generator.generateDrug();
                kieSession.insert(drugSample);
            }
            kieSession.fireAllRules();
            Collection<DiagnosisResult> diagnosisResultList = (Collection<DiagnosisResult>) kieSession.getObjects(new ClassObjectFilter(DiagnosisResult.class));
            if (!diagnosisResultList.isEmpty()) {
                DiagnosisResult diagnosisResult = diagnosisResultList.iterator().next();
                System.out.println("Diagnosis result: " + diagnosisResult);
                kieSession.dispose();
                return diagnosisResult;
            }
            // sleep for 0.5 seconds
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }



}
