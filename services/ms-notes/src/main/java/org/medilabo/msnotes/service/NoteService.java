package org.medilabo.msnotes.service;


import org.medilabo.msnotes.document.Note;
import org.medilabo.msnotes.dto.NoteDto;
import org.medilabo.msnotes.model.GenericResponseModel;

import java.util.List;

public interface NoteService {

    List<Note> findAllNotes();

    Note findById(String id);

    List<Note> findAllByPatientId(int patientId);

    Note addNote(NoteDto noteDto);

    GenericResponseModel deleteNote(String id);
}
