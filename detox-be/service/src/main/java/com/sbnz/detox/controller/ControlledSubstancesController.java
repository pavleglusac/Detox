package com.sbnz.detox.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.sbnz.detox.service.ControlledSubstancesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/controlled-substances")
public class ControlledSubstancesController {


    @Autowired
    private ControlledSubstancesService controlledSubstancesService;

    @PatchMapping("/add")
    public Object addControlledSubstanceSymptom(@RequestParam("diagnosisId") Long diagnosisId,
                                              @RequestBody JsonPatch patch) {
        return controlledSubstancesService.patchControlledSubstanceSymptom(diagnosisId, patch);
    }
}
