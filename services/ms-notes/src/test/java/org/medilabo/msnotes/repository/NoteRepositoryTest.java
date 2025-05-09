package org.medilabo.msnotes.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.medilabo.msnotes.document.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@TestPropertySource(properties = {
        "spring.mongodb.embedded.version=4.0.21",
        "spring.data.mongodb.database=test_db",
        "de.flapdoodle.mongodb.embedded.version=4.0.21",
        "spring.data.mongodb.auto-index-creation=true"
})
class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    private Note note1;

    @BeforeEach
    void setUp() {
        // Clear the repository before each test
        noteRepository.deleteAll();

        // Create test notes
        note1 = new Note();
        note1.setPatientId(1);
        note1.setContent("Note content for patient 1");
        note1.setDate(new Date());
        note1.setCreatedBy("test_user");

        Note note2 = new Note();
        note2.setPatientId(1);
        note2.setContent("Another note for patient 1");
        note2.setDate(new Date());
        note2.setCreatedBy("test_user");

        Note note3 = new Note();
        note3.setPatientId(2);
        note3.setContent("Note content for patient 2");
        note3.setDate(new Date());
        note3.setCreatedBy("test_user");

        // Save test notes
        noteRepository.saveAll(List.of(note1, note2, note3));
    }

    @Test
    void findAllByPatientId_shouldReturnNotesForSpecificPatient() {
        // Act
        List<Note> patientNotes = noteRepository.findAllByPatientId(1);

        // Assert
        assertThat(patientNotes).isNotNull();
        assertThat(patientNotes).hasSize(2);
        assertThat(patientNotes).allMatch(note -> note.getPatientId() == 1);
        assertThat(patientNotes).extracting(Note::getContent)
                .containsExactlyInAnyOrder("Note content for patient 1", "Another note for patient 1");
    }

    @Test
    void findAllByPatientId_shouldReturnEmptyList_whenPatientHasNoNotes() {
        // Act
        List<Note> patientNotes = noteRepository.findAllByPatientId(3);

        // Assert
        assertThat(patientNotes).isNotNull();
        assertThat(patientNotes).isEmpty();
    }

    @Test
    void findAllByPatientId_shouldNotReturnNotesForDifferentPatients() {
        // Act
        List<Note> patientNotes = noteRepository.findAllByPatientId(1);

        // Assert
        assertThat(patientNotes).isNotNull();
        assertThat(patientNotes).hasSize(2);
        assertThat(patientNotes).noneMatch(note -> note.getPatientId() == 2);
        assertThat(patientNotes).extracting(Note::getContent)
                .doesNotContain("Note content for patient 2");
    }

    @Test
    void findAll_shouldReturnAllNotes() {
        // Act
        List<Note> allNotes = noteRepository.findAll();

        // Assert
        assertThat(allNotes).isNotNull();
        assertThat(allNotes).hasSize(3);
    }

    @Test
    void save_shouldCreateNewNote() {
        // Arrange
        Note newNote = new Note();
        newNote.setPatientId(3);
        newNote.setContent("New note for patient 3");
        newNote.setDate(new Date());
        newNote.setCreatedBy("test_user");

        // Act
        Note savedNote = noteRepository.save(newNote);

        // Assert
        assertThat(savedNote).isNotNull();
        assertThat(savedNote.getId()).isNotNull();
        assertThat(savedNote.getPatientId()).isEqualTo(3);
        assertThat(savedNote.getContent()).isEqualTo("New note for patient 3");

        // Verify it was actually saved
        List<Note> patientNotes = noteRepository.findAllByPatientId(3);
        assertThat(patientNotes).hasSize(1);
        assertThat(patientNotes.getFirst().getPatientId()).isEqualTo(savedNote.getPatientId());
        assertThat(patientNotes.getFirst().getContent()).isEqualTo(savedNote.getContent());
        assertThat(patientNotes.getFirst().getDate()).isEqualTo(savedNote.getDate());
        assertThat(patientNotes.getFirst().getCreatedBy()).isEqualTo(savedNote.getCreatedBy());
    }

    @Test
    void deleteById_shouldRemoveNote() {
        // Arrange
        Note savedNote = noteRepository.save(note1);
        String noteId = savedNote.getId();

        // Act
        noteRepository.deleteById(noteId);

        // Assert
        assertThat(noteRepository.findById(noteId)).isEmpty();
    }
}