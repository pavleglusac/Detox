package com.sbnz.detox.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.sbnz.detox.model.DiagnosisResponse;
import com.sbnz.detox.model.DiagnosisResult;
import com.sbnz.detox.service.ControlledSubstancesService;
import com.sbnz.detox.service.GasChromatographyDrugsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/controlled-substances")
public class ControlledSubstancesController {


    @Autowired
    private ControlledSubstancesService controlledSubstancesService;

    @Autowired
    private GasChromatographyDrugsService gasChromatographyDrugsService;

    @PatchMapping("/add")
    public DiagnosisResponse addControlledSubstanceSymptom(@RequestParam("diagnosisId") Long diagnosisId,
                                                           @RequestBody JsonPatch patch) {
        return controlledSubstancesService.patchControlledSubstanceSymptom(diagnosisId, patch);
    }

    @GetMapping("/run-gas-chromatography")
    public DiagnosisResult runGasChromatography() throws IllegalAccessException, InstantiationException, FileNotFoundException {
    	return gasChromatographyDrugsService.runGasChromatography();
    }
}
