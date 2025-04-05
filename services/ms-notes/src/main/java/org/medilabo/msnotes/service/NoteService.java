package org.medilabo.msnotes.service;


import org.medilabo.msnotes.document.Note;
import org.medilabo.msnotes.dto.NoteDto;

import java.util.List;

public interface NoteService {

    List<Note> findAllNotes();

    Note findNoteById(String id);

    List<Note> findAllNotesByPatId(int patId);

    Note addNote(NoteDto noteDto);
}
