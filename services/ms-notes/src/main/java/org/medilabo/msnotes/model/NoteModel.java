package org.medilabo.msnotes.model;

public record NoteModel(String id, int patId, String patient, String content) {
}
