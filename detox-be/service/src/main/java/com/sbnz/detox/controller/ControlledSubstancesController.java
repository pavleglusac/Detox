package com.sbnz.detox.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.sbnz.detox.model.DiagnosisResponse;
import com.sbnz.detox.model.DiagnosisResult;
import com.sbnz.detox.service.ControlledSubstancesService;
import com.sbnz.detox.service.GasChromatographyDrugsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // endpoint that accepts excel file as blob of type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' and saves it
    // to the server
    @PostMapping("/configure")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {

        // save multipart file to server
        controlledSubstancesService.saveFile(file);

        return ResponseEntity.ok().build();
    }

    // return excel file from server
    @GetMapping("/configure")
    public ResponseEntity<?> getFile() {
        return controlledSubstancesService.getFile();
    }





}
