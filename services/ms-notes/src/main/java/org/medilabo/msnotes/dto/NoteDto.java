package org.medilabo.msnotes.dto;

public record NoteDto(int patientId, String content, String createdBy) {
}
