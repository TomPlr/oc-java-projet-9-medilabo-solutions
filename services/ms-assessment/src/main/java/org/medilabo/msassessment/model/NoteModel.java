package org.medilabo.msassessment.model;

import java.util.Date;

public record NoteModel(String id, int patientId, String content, Date date, String createdBy) {
}
