package com.sbnz.detox.service;

import com.sbnz.detox.model.Greeting;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

    private KieContainer kieContainer;

    @Autowired
    public SampleService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;

        // add a string to the session
        KieSession kieSession = kieContainer.newKieSession("cepKsession");
        kieSession.insert("Djes ba");
        kieSession.fireAllRules();
        Greeting greeting = new Greeting("mjaujork sad je u krizi");
        kieSession.insert(greeting);
        kieSession.fireAllRules();
    }
}
