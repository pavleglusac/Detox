package com.sbnz.detox.controller;


import com.sbnz.detox.service.QueriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/queries")
public class QueriesController {

    @Autowired
    private QueriesService queriesService;

    @GetMapping("/potential-heavy-drugs-users")
    public List<String> getPotentialHeavyDrugsUsers() throws IllegalAccessException {
        return queriesService.getPotentialHeavyDrugsUsers();
    }

    @GetMapping("/further-tests")
    public List<String> getFurtherTests(@RequestParam("username") String username) throws IllegalAccessException {
        return queriesService.furtherTestsForUser(username);
    }
    
}
