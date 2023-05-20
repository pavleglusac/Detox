package com.sbnz.detox.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.sbnz.detox.model.DiagnosisResponse;
import com.sbnz.detox.service.IndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/industry")
public class IndustryController {

    @Autowired
    private IndustryService industryService;

    @PatchMapping("/add")
    public DiagnosisResponse addIndustrySymptom(@RequestParam("diagnosisId") Long diagnosisId,
                                                           @RequestBody JsonPatch patch) {
        return industryService.patchIndustrySymptom(diagnosisId, patch);
    }

}
