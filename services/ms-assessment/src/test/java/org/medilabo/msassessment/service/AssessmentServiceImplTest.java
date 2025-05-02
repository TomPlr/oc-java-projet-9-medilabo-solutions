package org.medilabo.msassessment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.msassessment.dto.PatientDto;
import org.medilabo.msassessment.model.GenderModel;
import org.medilabo.msassessment.model.NoteModel;
import org.medilabo.msassessment.model.RiskLevelModel;
import org.medilabo.msassessment.service.impl.AssessmentServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceImplTest {

    @Mock
    private FileReaderService fileReaderService;

    @InjectMocks
    private AssessmentServiceImpl assessmentService;

    @BeforeEach
    void setUp() throws IOException {
        // Arrange - Read trigger terms from the actual file
        Resource resource = new ClassPathResource("trigger-terms.txt");
        List<String> triggerTerms = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                triggerTerms.add(line.toLowerCase());
            }
        }

        when(fileReaderService.getTriggerTerms()).thenReturn(triggerTerms);
    }

    @Test
    void evaluateRisk_PatientOver30_NoTriggers_ShouldReturnNoneRisk() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(40);
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.M, Collections.emptyList());

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.NONE);
    }

    @Test
    void evaluateRisk_PatientOver30_OneTrigger_ShouldReturnNoneRisk() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(40);
        List<NoteModel> notes = List.of(
                new NoteModel("1", 1, "Le patient présente un comportement anormal", new Date(), "Dr. Dupont")
        );
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.M, notes);

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.NONE);
    }

    @Test
    void evaluateRisk_PatientOver30_ThreeTriggers_ShouldReturnBorderlineRisk() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(40);
        List<NoteModel> notes = List.of(
                new NoteModel("1", 1, "Le patient présente un comportement anormal", new Date(), "Dr. Dupont"),
                new NoteModel("2", 1, "Le patient se plaint de vertiges et a un taux de cholestérol élevé", new Date(), "Dr. Martin")
        );
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.M, notes);

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.BORDERLINE);
    }

    @Test
    void evaluateRisk_PatientOver30_SixTriggers_ShouldReturnInDangerRisk() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(40);
        List<NoteModel> notes = List.of(
                new NoteModel("1", 1, "Le patient présente un comportement anormal et des vertiges", new Date(), "Dr. Dupont"),
                new NoteModel("2", 1, "Le patient a un taux de cholestérol élevé et est fumeur", new Date(), "Dr. Martin"),
                new NoteModel("3", 1, "Les tests récents montrent des niveaux élevés de microalbumine et d'hémoglobine a1c", new Date(), "Dr. Bernard")
        );
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.M, notes);

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.IN_DANGER);
    }

    @Test
    void evaluateRisk_PatientOver30_EightTriggers_ShouldReturnEarlyOnsetRisk() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(40);
        List<NoteModel> notes = List.of(
                new NoteModel("1", 1, "Le patient présente un comportement anormal et des vertiges", new Date(), "Dr. Dupont"),
                new NoteModel("2", 1, "Le patient a un taux de cholestérol élevé, est fumeur et a eu une rechute", new Date(), "Dr. Martin"),
                new NoteModel("3", 1, "Les tests récents montrent des niveaux élevés de microalbumine et d'hémoglobine a1c", new Date(), "Dr. Bernard"),
                new NoteModel("4", 1, "Le patient a eu une réaction aux médicaments et des anticorps ont été détectés", new Date(), "Dr. Petit")
        );
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.M, notes);

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.EARLY_ONSET);
    }

    @Test
    void evaluateRisk_MaleUnder30_NoTriggers_ShouldReturnNoneRisk() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(25);
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.M, Collections.emptyList());

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.NONE);
    }

    @Test
    void evaluateRisk_MaleUnder30_ThreeTriggers_ShouldReturnInDangerRisk() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(25);
        List<NoteModel> notes = List.of(
                new NoteModel("1", 1, "Le patient présente un comportement anormal", new Date(), "Dr. Dupont"),
                new NoteModel("2", 1, "Le patient se plaint de vertiges et a un taux de cholestérol élevé", new Date(), "Dr. Martin")
        );
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.M, notes);

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.IN_DANGER);
    }

    @Test
    void evaluateRisk_MaleUnder30_SixTriggers_ShouldReturnEarlyOnsetRisk() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(25);
        List<NoteModel> notes = List.of(
                new NoteModel("1", 1, "Le patient présente un comportement anormal et des vertiges", new Date(), "Dr. Dupont"),
                new NoteModel("2", 1, "Le patient a un taux de cholestérol élevé et est fumeur", new Date(), "Dr. Martin"),
                new NoteModel("3", 1, "Les tests récents montrent des niveaux élevés de microalbumine et d'hémoglobine a1c", new Date(), "Dr. Bernard")
        );
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.M, notes);

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.EARLY_ONSET);
    }

    @Test
    void evaluateRisk_FemaleUnder30_NoTriggers_ShouldReturnNoneRisk() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(25);
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.F, Collections.emptyList());

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.NONE);
    }

    @Test
    void evaluateRisk_FemaleUnder30_TwoTriggers_ShouldReturnNoneRisk() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(25);
        List<NoteModel> notes = List.of(
                new NoteModel("1", 1, "La patiente présente un comportement anormal", new Date(), "Dr. Dupont"),
                new NoteModel("2", 1, "La patiente se plaint de vertiges", new Date(), "Dr. Martin")
        );
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.F, notes);

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.NONE);
    }

    @Test
    void evaluateRisk_FemaleUnder30_FourTriggers_ShouldReturnInDangerRisk() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(25);
        List<NoteModel> notes = List.of(
                new NoteModel("1", 1, "La patiente présente un comportement anormal", new Date(), "Dr. Dupont"),
                new NoteModel("2", 1, "La patiente se plaint de vertiges et a un taux de cholestérol élevé", new Date(), "Dr. Martin"),
                new NoteModel("3", 1, "La patiente est fumeuse", new Date(), "Dr. Bernard")
        );
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.F, notes);

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.IN_DANGER);
    }

    @Test
    void evaluateRisk_FemaleUnder30_SevenTriggers_ShouldReturnEarlyOnsetRisk() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(25);
        List<NoteModel> notes = List.of(
                new NoteModel("1", 1, "La patiente présente un comportement anormal et des vertiges", new Date(), "Dr. Dupont"),
                new NoteModel("2", 1, "La patiente a un taux de cholestérol élevé et est fumeuse", new Date(), "Dr. Martin"),
                new NoteModel("3", 1, "Les tests récents montrent des niveaux élevés de microalbumine et d'hémoglobine a1c", new Date(), "Dr. Bernard"),
                new NoteModel("4", 1, "La patiente a eu une réaction aux médicaments", new Date(), "Dr. Petit")
        );
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.F, notes);

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.EARLY_ONSET);
    }

    @Test
    void evaluateRisk_CaseInsensitivityOfTriggerTerms() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(40);
        List<NoteModel> notes = List.of(
                new NoteModel("1", 1, "Le patient présente un comportement ANORMAL", new Date(), "Dr. Dupont"),
                new NoteModel("2", 1, "Le patient se plaint de VerTiGeS", new Date(), "Dr. Martin")
        );
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.M, notes);

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.BORDERLINE);
    }

    @Test
    void evaluateRisk_GenderSpecificTriggerTerms() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(25);
        List<NoteModel> notes = List.of(
                new NoteModel("1", 1, "La patiente est fumeuse", new Date(), "Dr. Dupont")
        );
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.F, notes);

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.NONE);
    }

    @Test
    void evaluateRisk_MultipleOccurrencesOfSameTriggerTerm_CountedOnce() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(40);
        List<NoteModel> notes = List.of(
                new NoteModel("1", 1, "Le patient présente un comportement anormal. Ce comportement anormal a continué.", new Date(), "Dr. Dupont")
        );
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.M, notes);

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.NONE); // Only one trigger term should be counted
    }

    @Test
    void evaluateRisk_AccentInsensitivity() {
        // Arrange
        String dateOfBirth = getDateOfBirthForAge(40);
        List<NoteModel> notes = List.of(
                new NoteModel("1", 1, "Le patient a un cholestérol élevé", new Date(), "Dr. Dupont")
        );
        PatientDto patientDto = new PatientDto(dateOfBirth, GenderModel.M, notes);

        // Act
        RiskLevelModel result = assessmentService.evaluateRisk(patientDto);

        // Assert
        assertThat(result).isEqualTo(RiskLevelModel.NONE);
    }

    /**
     * Helper method to generate a date of birth for a specified age
     */
    private String getDateOfBirthForAge(int age) {
        return LocalDate.now().minusYears(age).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}