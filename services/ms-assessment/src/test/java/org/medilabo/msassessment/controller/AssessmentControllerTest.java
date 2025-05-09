package org.medilabo.msassessment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.msassessment.dto.PatientDto;
import org.medilabo.msassessment.model.GenderModel;
import org.medilabo.msassessment.model.NoteModel;
import org.medilabo.msassessment.model.RiskLevelModel;
import org.medilabo.msassessment.service.AssessmentService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssessmentControllerTest {

    @Mock
    private AssessmentService assessmentService;

    @InjectMocks
    private AssessmentController assessmentController;

    private PatientDto patientDto;

    @BeforeEach
    void setUp() {
        List<NoteModel> notes = new ArrayList<>();
        NoteModel noteTest = new NoteModel("1", 1, "Le patient présente un comportement anormal", new Date(), "Dr. Dupont");

        notes.add(noteTest);

        patientDto = new PatientDto("1980-01-01", GenderModel.M, notes);
    }

    RiskLevelModel riskLevelModel = RiskLevelModel.NONE;

    @Test
    void assessment_shouldReturnRiskLevelWithStatusOk() {
        // Arrange
        when(assessmentService.evaluateRisk(patientDto)).thenReturn(riskLevelModel);

        // Act
        ResponseEntity<RiskLevelModel> response = assessmentController.assessment(patientDto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(riskLevelModel);
        verify(assessmentService).evaluateRisk(patientDto);
    }

    @Test
    void assessment_shouldDelegateToAssessmentService() {
        // Arrange
        when(assessmentService.evaluateRisk(patientDto)).thenReturn(riskLevelModel);

        // Act
        ResponseEntity<RiskLevelModel> response = assessmentController.assessment(patientDto);

        // Assert
        verify(assessmentService).evaluateRisk(patientDto);
        assertThat(response.getBody()).isSameAs(riskLevelModel);
    }
}