package org.medilabo.msassessment.dto;

import org.medilabo.msassessment.model.GenderModel;
import org.medilabo.msassessment.model.NoteModel;

import java.util.List;

public record PatientDto(String dateOfBirth, GenderModel gender, List<NoteModel> notes) {
}
