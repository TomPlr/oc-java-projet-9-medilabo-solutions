package org.medilabo.msassessment.service;


import org.medilabo.msassessment.dto.PatientDto;
import org.medilabo.msassessment.model.RiskLevelModel;

public interface AssessmentService {

    /**
     * Evaluates the diabetes risk level for a patient based on their medical notes,
     * age, and gender
     *
     * @param patientDto the patient to evaluate
     * @return the assessed risk level
     */
    RiskLevelModel evaluateRisk(PatientDto patientDto);
}