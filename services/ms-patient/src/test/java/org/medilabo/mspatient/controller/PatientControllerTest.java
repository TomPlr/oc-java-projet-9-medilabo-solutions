package org.medilabo.mspatient.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.mspatient.assembler.PatientAssembler;
import org.medilabo.mspatient.dto.AddressDto;
import org.medilabo.mspatient.dto.PatientDto;
import org.medilabo.mspatient.entity.Address;
import org.medilabo.mspatient.entity.Patient;
import org.medilabo.mspatient.model.GenericResponseModel;
import org.medilabo.mspatient.model.PatientModel;
import org.medilabo.mspatient.service.PatientService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @Spy
    private PatientAssembler patientAssembler;

    @InjectMocks
    private PatientController patientController;

    private Patient testPatient;
    private PatientModel testPatientModel;
    private PatientDto testPatientDto;
    private List<Patient> patientList;

    @BeforeEach
    void setUp() {

        Address testAddress = new Address();

        testAddress.setAddressId(1);
        testAddress.setStreet("123 Test St");
        testAddress.setCity("Test City");
        testAddress.setPostalCode("12345");

        testPatient = new Patient();
        testPatient.setId(1);
        testPatient.setFirstName("John");
        testPatient.setLastName("Doe");
        testPatient.setDateOfBirth("1980-01-01");
        testPatient.setGender("M");
        testPatient.setPhoneNumber("555-123-4567");
        testPatient.setAddress(testAddress);


        testPatientModel = new PatientModel(1, "Doe", "John", "1980-01-01", "M", testAddress, "555-123-4567");

        AddressDto addressDto = new AddressDto("123 Test St", "Test City", "12345");
        testPatientDto = new PatientDto("Doe", "John", LocalDate.of(1980, 1, 1), "M", addressDto, "555-123-4567");

        patientList = List.of(testPatient);
    }

    @Test
    void getAllPatients_ReturnsListOfPatients() {
        // Arrange
        when(patientService.findAll()).thenReturn(patientList);

        // Act
        ResponseEntity<List<PatientModel>> response = patientController.getAllPatients();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(patientService).findAll();
        verify(patientAssembler, times(1)).toModel(any(Patient.class));
    }

    @Test
    void getAllPatients_ReturnsEmptyList_WhenNoPatients() {
        // Arrange
        when(patientService.findAll()).thenReturn(List.of());

        // Act
        ResponseEntity<List<PatientModel>> response = patientController.getAllPatients();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void addPatient_ReturnsCreatedPatient() {
        // Arrange
        when(patientService.addPatient(any(PatientDto.class))).thenReturn(testPatientModel);

        // Act
        ResponseEntity<PatientModel> response = patientController.addPatient(testPatientDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testPatientModel, response.getBody());
        verify(patientService).addPatient(testPatientDto);
    }

    @Test
    void getPatient_ReturnsPatient_WhenPatientExists() {
        // Arrange
        when(patientService.findPatient(1)).thenReturn(Optional.of(testPatient));

        // Act
        ResponseEntity<PatientModel> response = patientController.getPatient(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPatientModel, response.getBody());
        verify(patientService).findPatient(1);
    }

    @Test
    void getPatient_ThrowsException_WhenPatientDoesNotExist() {
        // Arrange
        when(patientService.findPatient(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> patientController.getPatient(999)
        );

        assertEquals("This patient does not exist", exception.getMessage());
        verify(patientService).findPatient(999);
    }

    @Test
    void updatePatient_ReturnsUpdatedPatient() {
        // Arrange
        when(patientService.updatePatient(anyInt(), any(PatientDto.class))).thenReturn(testPatientModel);

        // Act
        ResponseEntity<PatientModel> response = patientController.updatePatient(1, testPatientDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPatientModel, response.getBody());
        verify(patientService).updatePatient(1, testPatientDto);
    }

    @Test
    void deletePatient_ReturnsSuccessResponse() {
        // Arrange
        GenericResponseModel successResponse = new GenericResponseModel(true, "Patient deleted successfully");
        when(patientService.deletePatient(anyInt())).thenReturn(successResponse);

        // Act
        ResponseEntity<GenericResponseModel> response = patientController.deletePatient(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().success());
        assertEquals("Patient deleted successfully", response.getBody().details());
        verify(patientService).deletePatient(1);
    }
}