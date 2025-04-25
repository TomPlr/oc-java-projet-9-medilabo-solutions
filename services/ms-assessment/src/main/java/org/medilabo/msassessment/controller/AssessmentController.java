package org.medilabo.msassessment.controller;

import org.medilabo.msassessment.dto.PatientDto;
import org.medilabo.msassessment.model.NoteModel;
import org.medilabo.msassessment.model.PatientModel;
import org.medilabo.msassessment.model.RiskLevelModel;
import org.medilabo.msassessment.service.AssessmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessment")
public class AssessmentController {

    private final AssessmentService assessmentService;


    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @PostMapping
    public ResponseEntity<RiskLevelModel> assessment(@RequestBody PatientDto patientDto) {
        return new ResponseEntity<>(assessmentService.evaluateRisk(patientDto), HttpStatus.OK);
    }
}
