package org.medilabo.msnotes.repository;

import org.medilabo.msnotes.document.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    /**
     * Finds all notes associated with a specific patient.
     *
     * @param patId the patient identifier
     * @return a list of notes for the specified patient
     */
    List<Note> findAllByPatientId(int patId);
}