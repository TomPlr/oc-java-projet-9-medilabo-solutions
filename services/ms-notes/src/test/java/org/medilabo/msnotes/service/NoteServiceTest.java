package org.medilabo.msnotes.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.msnotes.document.Note;
import org.medilabo.msnotes.dto.NoteDto;
import org.medilabo.msnotes.mapper.NoteMapper;
import org.medilabo.msnotes.model.GenericResponseModel;
import org.medilabo.msnotes.repository.NoteRepository;
import org.medilabo.msnotes.service.impl.NoteServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Spy
    private NoteMapper noteMapper;

    @InjectMocks
    private NoteServiceImpl noteService;

    private Note testNote;
    private NoteDto testNoteDto;

    @BeforeEach
    void setUp() {
        testNote = new Note();
        testNote.setId("67caf22ab9bda8fe4a1015be");
        testNote.setPatientId(1);
        testNote.setContent("Test content");
        testNote.setDate(new Date());
        testNote.setCreatedBy("test_user");

        testNoteDto = new NoteDto(1, "Test content", "test_user");
    }

    @Test
    void findAllNotes_shouldReturnAllNotes() {
        // Arrange
        List<Note> expectedNotes = List.of(testNote);
        when(noteRepository.findAll()).thenReturn(expectedNotes);

        // Act
        List<Note> actualNotes = noteService.findAllNotes();

        // Assert
        assertThat(actualNotes).isEqualTo(expectedNotes);
        verify(noteRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnNote_whenNoteExists() {
        // Arrange
        when(noteRepository.findById(anyString())).thenReturn(Optional.of(testNote));

        // Act
        Note foundNote = noteService.findById("67caf22ab9bda8fe4a1015be");

        // Assert
        assertThat(foundNote).isEqualTo(testNote);
        verify(noteRepository, times(1)).findById("67caf22ab9bda8fe4a1015be");
    }

    @Test
    void findById_shouldReturnNull_whenNoteDoesNotExist() {
        // Arrange
        when(noteRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        Note foundNote = noteService.findById("nonexistent_id");

        // Assert
        assertThat(foundNote).isNull();
        verify(noteRepository, times(1)).findById("nonexistent_id");
    }

    @Test
    void findAllByPatientId_shouldReturnPatientNotes() {
        // Arrange
        List<Note> expectedNotes = List.of(testNote);
        when(noteRepository.findAllByPatientId(anyInt())).thenReturn(expectedNotes);

        // Act
        List<Note> actualNotes = noteService.findAllByPatientId(1);

        // Assert
        assertThat(actualNotes).isEqualTo(expectedNotes);
        verify(noteRepository, times(1)).findAllByPatientId(1);
    }

    @Test
    void addNote_shouldCreateAndReturnNewNote() {
        // Arrange
        when(noteMapper.toEntity(any(NoteDto.class))).thenReturn(testNote);
        when(noteRepository.save(any(Note.class))).thenReturn(testNote);

        // Act
        Note createdNote = noteService.addNote(testNoteDto);

        // Assert
        assertThat(createdNote).isEqualTo(testNote);
        assertThat(createdNote.getDate()).isNotNull();
        verify(noteMapper, times(1)).toEntity(testNoteDto);
        verify(noteRepository, times(1)).save(testNote);
    }

    @Test
    void deleteNote_shouldReturnSuccessResponse_whenDeletionSucceeds() {
        // Arrange
        doNothing().when(noteRepository).deleteById(anyString());

        // Act
        GenericResponseModel response = noteService.deleteNote("67caf22ab9bda8fe4a1015be");

        // Assert
        assertThat(response.success()).isTrue();
        assertThat(response.details()).isEqualTo("Note deleted successfully");
        verify(noteRepository, times(1)).deleteById("67caf22ab9bda8fe4a1015be");
    }

    @Test
    void deleteNote_shouldReturnFailureResponse_whenDeletionFails() {
        // Arrange
        doThrow(new RuntimeException("Delete failed")).when(noteRepository).deleteById(anyString());

        // Act
        GenericResponseModel response = noteService.deleteNote("67caf22ab9bda8fe4a1015be");

        // Assert
        assertThat(response.success()).isFalse();
        assertThat(response.details()).isEqualTo("Delete failed");
        verify(noteRepository, times(1)).deleteById("67caf22ab9bda8fe4a1015be");
    }
}