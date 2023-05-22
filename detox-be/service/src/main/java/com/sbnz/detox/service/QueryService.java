package com.sbnz.detox.service;

import com.sbnz.detox.model.queries.QueryModel;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryService {

    KieContainer kieContainer;

    @Autowired
    public QueryService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
        KieSession kieSession = kieContainer.newKieSession("ksession");

        kieSession.insert(new QueryModel("Kokain", "urineTest", "PRESENCE_BENZOILECGONINE"));
        kieSession.insert(new QueryModel("urineTest", "AffectsCNS", "false"));
        kieSession.insert(new QueryModel("AffectsCNS", "chromatographicImunoassayTest", "DRUGS"));
    }

}
