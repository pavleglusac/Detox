package com.sbnz.detox.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.sbnz.detox.model.DiagnosisResponse;
import com.sbnz.detox.model.DiagnosisResult;
import com.sbnz.detox.service.GasChromatographyIndustryService;
import com.sbnz.detox.service.IndustryService;
import com.sbnz.detox.service.SpectophotometryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/industry")
public class IndustryController {

    @Autowired
    private IndustryService industryService;

    @Autowired
    private GasChromatographyIndustryService gasChromatographyIndustryService;

    @Autowired
    private SpectophotometryService spectophotometryService;

    @PatchMapping("/add")
    public DiagnosisResponse addIndustrySymptom(@RequestParam("diagnosisId") Long diagnosisId,
                                                           @RequestBody JsonPatch patch) {
        return industryService.patchIndustrySymptom(diagnosisId, patch);
    }

    @GetMapping("/run-gas-chromatography")
    public DiagnosisResult runGasChromatography() throws IllegalAccessException, InstantiationException, FileNotFoundException {
        return gasChromatographyIndustryService.runGasChromatography();
    }

    @PostMapping("gas-chromatography/configure")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {

        // save multipart file to server
        industryService.saveFileGC(file);

        return ResponseEntity.ok().build();
    }

    // return excel file from server
    @GetMapping("gas-chromatography/configure")
    public ResponseEntity<?> getFile() {
        return industryService.getFileGC();
    }

    @GetMapping("/run-spectophotometry")
    public DiagnosisResult runSpectrophotometry() throws IllegalAccessException, InstantiationException, FileNotFoundException {
        return spectophotometryService.runSpectrophotometry();
    }

    @PostMapping("spectrophotometry/configure")
    public ResponseEntity<?> handleFileUploadSpectrophotometry(@RequestParam("file") MultipartFile file) {

        // save multipart file to server
        industryService.saveFileSpectro(file);

        return ResponseEntity.ok().build();
    }

    // return excel file from server
    @GetMapping("spectrophotometry/configure")
    public ResponseEntity<?> getFileSpectophotometry() {
        return industryService.getFileSpectro();
    }

}
