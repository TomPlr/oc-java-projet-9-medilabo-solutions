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
        int triggerCount = Math.toIntExact(countTriggerTerms(patientDto.notes()));

        if (getAge(patientDto.dateOfBirth()) > 30) {
            return switch ((Integer) triggerCount) {
                case Integer i when i < 2 -> RiskLevelModel.NONE;
                case Integer i when i < 6 -> RiskLevelModel.BORDERLINE;
                case Integer i when i < 8 -> RiskLevelModel.IN_DANGER;
                default -> RiskLevelModel.EARLY_ONSET;
            };
        } else {
            return switch (patientDto.gender()) {
                case GenderModel.M -> switch ((Integer) triggerCount) {
                    case Integer i when i < 2 -> RiskLevelModel.NONE;
                    case Integer i when i < 6 -> RiskLevelModel.IN_DANGER;
                    default -> RiskLevelModel.EARLY_ONSET;
                };
                case GenderModel.F -> switch ((Integer) triggerCount) {
                    case Integer i when i < 3 -> RiskLevelModel.NONE;
                    case Integer i when i < 7 -> RiskLevelModel.IN_DANGER;
                    default -> RiskLevelModel.EARLY_ONSET;
                };
            };
        }
    }

    private long countTriggerTerms(List<NoteModel> notes) {
        if (TRIGGER_TERMS.isEmpty()) {
            TRIGGER_TERMS.addAll(fileReaderService.getTriggerTerms());
        }

        return TRIGGER_TERMS.stream().map(String::toLowerCase).filter(term -> isTermInNotes(term, notes)).count();
    }

    private int getAge(String dateOfBirth) {
        return LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .until(LocalDate.now()).getYears();
    }

    private boolean isTermInNotes(String term, List<NoteModel> notes) {
        return notes.stream()
                .anyMatch(note -> note.content().toLowerCase().contains(term.toLowerCase()));
    }
}
