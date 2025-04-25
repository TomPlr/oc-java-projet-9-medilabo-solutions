package org.medilabo.msassessment.service.impl;


import org.medilabo.msassessment.dto.PatientDto;
import org.medilabo.msassessment.model.GenderModel;
import org.medilabo.msassessment.model.NoteModel;
import org.medilabo.msassessment.model.RiskLevelModel;
import org.medilabo.msassessment.service.AssessmentService;
import org.medilabo.msassessment.service.FileReaderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssessmentServiceImpl implements AssessmentService {

    private final FileReaderService fileReaderService;
    private final List<String> TRIGGER_TERMS = new ArrayList<>();

    public AssessmentServiceImpl(FileReaderService fileReaderService) {
        this.fileReaderService = fileReaderService;
    }

    public RiskLevelModel evaluateRisk(PatientDto patientDto) {
        int triggerCount = countTriggerTerms(patientDto.notes());

        if (triggerCount == 0) return RiskLevelModel.NONE;
        if (getAge(patientDto.dateOfBirth()) > 30) {
            return switch (triggerCount) {
                case 2, 3, 4, 5 -> RiskLevelModel.BORDERLINE;
                case 6, 7 -> RiskLevelModel.IN_DANGER;
                default -> RiskLevelModel.EARLY_ONSET;
            };
        } else {
            return switch (patientDto.gender()) {
                case GenderModel.M -> switch (triggerCount) {
                    case 3, 4 -> RiskLevelModel.IN_DANGER;
                    default -> RiskLevelModel.EARLY_ONSET;
                };
                case GenderModel.F -> triggerCount == 4 ? RiskLevelModel.IN_DANGER : RiskLevelModel.EARLY_ONSET;
            };
        }
    }

    private int countTriggerTerms(List<NoteModel> notes) {
        if (TRIGGER_TERMS.isEmpty()) {
            TRIGGER_TERMS.addAll(fileReaderService.getTriggerTerms());
        }

        String allNotes = notes.stream().map(NoteModel::content).reduce("", String::concat);

        return Math.toIntExact(TRIGGER_TERMS.stream().map(String::toLowerCase).filter(allNotes::contains).count());
    }

    private int getAge(String dateOfBirth) {
        return LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .until(LocalDate.now()).getYears();
    }

}
