package org.medilabo.msnotes.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.msnotes.assembler.NoteAssembler;
import org.medilabo.msnotes.document.Note;
import org.medilabo.msnotes.dto.NoteDto;
import org.medilabo.msnotes.model.GenericResponseModel;
import org.medilabo.msnotes.model.NoteModel;
import org.medilabo.msnotes.service.NoteService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    @Mock
    private NoteService noteService;

    @Spy
    private NoteAssembler noteAssembler;

    @InjectMocks
    private NoteController noteController;

    private Note testNote;
    private NoteModel testNoteModel;
    private NoteDto testNoteDto;

    @BeforeEach
    void setUp() {

        testNote = new Note();
        testNote.setId("67caf22ab9bda8fe4a1015be");
        testNote.setPatientId(1);
        testNote.setContent("Test content");
        testNote.setDate(new Date());
        testNote.setCreatedBy("test_user");

        testNoteModel = new NoteModel(
                "67caf22ab9bda8fe4a1015be", 1, "Test content", new Date(), "test_user");

        testNoteDto = new NoteDto(1, "Test content", "test_user");
    }

    @Test
    void findNotes_shouldReturnAllNotes() {
        // Arrange
        List<Note> notes = List.of(testNote);
        when(noteService.findAllNotes()).thenReturn(notes);

        // Act
        ResponseEntity<List<NoteModel>> response = noteController.findNotes();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().getFirst()).isEqualTo(testNoteModel);
    }

    @Test
    void findById_shouldReturnNoteById() {
        // Arrange
        when(noteService.findById(anyString())).thenReturn(testNote);

        // Act
        ResponseEntity<NoteModel> response = noteController.findById("67caf22ab9bda8fe4a1015be");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(testNoteModel);
    }

    @Test
    void findByPatientId_shouldReturnNotesByPatientId() {
        // Arrange
        List<Note> notes = List.of(testNote);
        when(noteService.findAllByPatientId(any(Integer.class))).thenReturn(notes);

        // Act
        ResponseEntity<List<NoteModel>> response = noteController.findByPatientId(1);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().getFirst()).isEqualTo(testNoteModel);
    }

    @Test
    @WithMockUser(authorities = "medical_staff")
    void addNote_shouldAddNoteAndReturnCreatedStatus() {
        // Arrange
        when(noteService.addNote(any(NoteDto.class))).thenReturn(testNote);

        // Act
        ResponseEntity<NoteModel> response = noteController.addNote(testNoteDto);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(testNoteModel);
    }

    @Test
    @WithMockUser(authorities = "medical_staff")
    void deleteNote_shouldDeleteNoteAndReturnOkStatus() {
        // Arrange
        GenericResponseModel successResponse = new GenericResponseModel(true, "Note deleted successfully");
        when(noteService.deleteNote(anyString())).thenReturn(successResponse);

        // Act
        ResponseEntity<GenericResponseModel> response = noteController.deleteNote("67caf22ab9bda8fe4a1015be");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(successResponse);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().success()).isTrue();
        assertThat(response.getBody().details()).isEqualTo("Note deleted successfully");
    }

    @Test
    @WithMockUser(authorities = "medical_staff")
    void deleteNote_shouldReturnErrorResponse_whenDeletionFails() {
        // Arrange
        GenericResponseModel errorResponse = new GenericResponseModel(false, "Error deleting note");
        when(noteService.deleteNote(anyString())).thenReturn(errorResponse);

        // Act
        ResponseEntity<GenericResponseModel> response = noteController.deleteNote("invalid_id");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(errorResponse);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().success()).isFalse();
        assertThat(response.getBody().details()).isEqualTo("Error deleting note");
    }
}