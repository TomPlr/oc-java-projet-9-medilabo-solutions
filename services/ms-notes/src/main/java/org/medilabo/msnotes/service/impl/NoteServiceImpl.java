package org.medilabo.msnotes.service.impl;

import org.medilabo.msnotes.document.Note;
import org.medilabo.msnotes.dto.NoteDto;
import org.medilabo.msnotes.mapper.NoteMapper;
import org.medilabo.msnotes.repository.NoteRepository;
import org.medilabo.msnotes.service.NoteService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }

    @Override
    public List<Note> findAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    public Note findById(String id) {
        return noteRepository.findById(id).orElse(null);
    }

    @Override
    public List<Note> findAllByPatientId(int patId) {
        return noteRepository.findAllByPatientId(patId);
    }

    @Override
    public Note addNote(NoteDto noteDto) {
        Note note = noteMapper.toEntity(noteDto);
        note.setDate(new Date());

        return noteRepository.save(note);
    }
}
