package org.medilabo.msnotes.service;

import org.medilabo.msnotes.document.Note;
import org.medilabo.msnotes.dto.NoteDto;
import org.medilabo.msnotes.model.GenericResponseModel;

import java.util.List;

public interface NoteService {

    /**
     * Retrieves all notes in the system.
     *
     * @return a list of all notes
     */
    List<Note> findAllNotes();

    /**
     * Finds a note by its unique identifier.
     *
     * @param id the note identifier
     * @return the note if found
     */
    Note findById(String id);

    /**
     * Retrieves all notes associated with a specific patient.
     *
     * @param patientId the patient identifier
     * @return a list of notes for the specified patient
     */
    List<Note> findAllByPatientId(int patientId);

    /**
     * Creates a new note in the system.
     *
     * @param noteDto the note data transfer object
     * @return the created note
     */
    Note addNote(NoteDto noteDto);

    /**
     * Deletes a note from the system.
     *
     * @param id the note identifier to delete
     * @return a response indicating success or failure of the operation
     */
    GenericResponseModel deleteNote(String id);
}