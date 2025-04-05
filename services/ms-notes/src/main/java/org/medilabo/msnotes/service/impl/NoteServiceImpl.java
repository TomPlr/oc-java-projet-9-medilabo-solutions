package org.medilabo.msnotes.service.impl;

import org.medilabo.msnotes.document.Note;
import org.medilabo.msnotes.dto.NoteDto;
import org.medilabo.msnotes.mapper.NoteMapper;
import org.medilabo.msnotes.repository.NoteRepository;
import org.medilabo.msnotes.service.NoteService;
import org.springframework.stereotype.Service;

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
    public Note findNoteById(String id) {
        return noteRepository.findById(id).orElse(null);
    }

    @Override
    public List<Note> findAllNotesByPatId(int patId) {
        return noteRepository.findAllByPatId(patId);
    }

    @Override
    public Note addNote(NoteDto noteDto) {
        return noteRepository.save(noteMapper.toEntity(noteDto));
    }
}
