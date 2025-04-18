package org.medilabo.msnotes.controller;

import org.medilabo.msnotes.assembler.NoteAssembler;
import org.medilabo.msnotes.dto.NoteDto;
import org.medilabo.msnotes.model.NoteModel;
import org.medilabo.msnotes.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;
    private final NoteAssembler noteAssembler;

    public NoteController(NoteService noteService, NoteAssembler noteAssembler) {
        this.noteService = noteService;
        this.noteAssembler = noteAssembler;
    }

    @GetMapping("/all")
    public ResponseEntity<List<NoteModel>> findNotes() {
        return new ResponseEntity<>(noteService.findAllNotes().stream().map(noteAssembler::toModel).toList(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteModel> findById(@PathVariable String id) {
        return new ResponseEntity<>(noteAssembler.toModel(noteService.findById(id)), HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<NoteModel>> findByPatientId(@PathVariable int patientId) {
        return new ResponseEntity<>(noteService.findAllByPatientId(patientId).stream().map(noteAssembler::toModel).toList(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NoteModel> addNote(@RequestBody NoteDto noteDto) {
        return new ResponseEntity<>(noteAssembler.toModel(noteService.addNote(noteDto)), HttpStatus.CREATED);
    }
}
