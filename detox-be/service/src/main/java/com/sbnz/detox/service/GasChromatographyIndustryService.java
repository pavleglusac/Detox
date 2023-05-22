package com.sbnz.detox.service;

import com.sbnz.detox.model.DiagnosisResult;
import com.sbnz.detox.model.gas_chromatography_industry.*;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


import java.io.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class GasChromatographyIndustryService {

    @Autowired
    private KieContainer kieContainer;

    class IndustryToxicGenerator<T extends IndustryToxicSample> {
        private final Class<T> clazz;
        private final int minValue;
        private final int maxValue;
        private final Random random = new Random();

        private final Double skipChances;

        public IndustryToxicGenerator(Class<T> clazz, int minValue, int maxValue, Double skipChances) {
            this.clazz = clazz;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.skipChances = skipChances;
        }

        public T generateIndustryToxin() throws IllegalAccessException, InstantiationException {
            T toxin = clazz.newInstance();
            toxin.setConcentration(generateConcentration());
            toxin.setTimestamp(new Date());
            return toxin;
        }

        public double generateConcentration() {
            return minValue + (maxValue - minValue) * random.nextDouble();
        }

        public boolean skip() {
            return random.nextDouble() < skipChances;
        }
    }

    List<GasChromatographyIndustryService.IndustryToxicGenerator> generators = List.of(
            new GasChromatographyIndustryService.IndustryToxicGenerator<>(VinylChloride.class, 0, 50, 0.1),
            new GasChromatographyIndustryService.IndustryToxicGenerator<>(Formaldehyde.class, 0, 80, 0.4),
            new GasChromatographyIndustryService.IndustryToxicGenerator<>(PCB.class, 0, 120, 0.4),
            new GasChromatographyIndustryService.IndustryToxicGenerator<>(Toluene.class, 0, 120, 0.4),
            new GasChromatographyIndustryService.IndustryToxicGenerator<>(Benzene.class, 0, 100, 0.0)
    );

    public DiagnosisResult runGasChromatography() throws IllegalAccessException, InstantiationException, FileNotFoundException {
        KieSession kieSession = getSessionFromTemplate();
        while (true) {
            for (GasChromatographyIndustryService.IndustryToxicGenerator generator : generators) {
                if (generator.skip()) {
                    continue;
                }
                IndustryToxicSample drugSample = generator.generateIndustryToxin();
                kieSession.insert(drugSample);
            }
            kieSession.fireAllRules();
            Collection<DiagnosisResult> diagnosisResultList = (Collection<DiagnosisResult>) kieSession.getObjects(new ClassObjectFilter(DiagnosisResult.class));
            if (!diagnosisResultList.isEmpty()) {
                DiagnosisResult diagnosisResult = new DiagnosisResult();
                diagnosisResult.setContent(concatenateDiagnosisResults(diagnosisResultList));
                return diagnosisResult;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private KieSession getSessionFromTemplate() throws FileNotFoundException {
        File file = new File("./kjar/src/main/resources/rules/industry/gas_chromatography_substances.drt");
        File dataFile = new File("./kjar/src/main/resources/industryGasTemplate.xlsx");
        InputStream template = new FileInputStream(file);
        InputStream data = new FileInputStream(dataFile);
        ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
        String drl = converter.compile(data, template, 2, 1);
        System.out.println(drl);
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
        return kieSession;
    }

    private String concatenateDiagnosisResults(Collection<DiagnosisResult> diagnosisResults) {
        StringBuilder sb = new StringBuilder();
        for (DiagnosisResult diagnosisResult : diagnosisResults) {
            sb.append(diagnosisResult.getContent());
            sb.append("\n");
        }
        return sb.toString();
    }

}
