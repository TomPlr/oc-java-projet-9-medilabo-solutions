package org.medilabo.msnotes.assembler;

import org.medilabo.msnotes.document.Note;
import org.medilabo.msnotes.model.NoteModel;
import org.springframework.stereotype.Component;

@Component
public class NoteAssembler {

    public NoteModel toModel(Note note) {
        return new NoteModel(note.getId(), note.getPatientId(), note.getContent(), note.getDate(), note.getCreatedBy());
    }
}
