package org.medilabo.mspatient.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.mspatient.assembler.PatientAssembler;
import org.medilabo.mspatient.dto.AddressDto;
import org.medilabo.mspatient.dto.PatientDto;
import org.medilabo.mspatient.entity.Address;
import org.medilabo.mspatient.entity.Patient;
import org.medilabo.mspatient.mapper.PatientMapper;
import org.medilabo.mspatient.model.GenericResponseModel;
import org.medilabo.mspatient.model.PatientModel;
import org.medilabo.mspatient.repository.PatientRepository;
import org.medilabo.mspatient.service.impl.PatientServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientAssembler patientAssembler;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientDto patientDto;
    private PatientModel patientModel;
    private Address address;
    private AddressDto addressDto;

    @BeforeEach
    void setUp() {
        addressDto = new AddressDto("123 Main St", "Test City", "12345");
        address = new Address();
        address.setAddressId(1);
        address.setStreet("123 Main St");
        address.setCity("Test City");
        address.setPostalCode("12345");

        patientDto = new PatientDto("Test", "Patient", LocalDate.of(1990, 1, 1), "M", addressDto, "123-456-7890");

        patient = new Patient();
        patient.setId(1);
        patient.setFirstName("Test");
        patient.setLastName("Patient");
        patient.setDateOfBirth(String.valueOf(LocalDate.of(1990, 1, 1)));
        patient.setGender("M");
        patient.setAddress(address);
        patient.setPhoneNumber("123-456-7890");

        patientModel = new PatientModel(1, "Test", "Patient", String.valueOf(LocalDate.of(1990, 1, 1)), "M", address, "123-456-7890");
    }

    @Test
    void findPatient_shouldReturnPatient_whenPatientExists() {
        // Arrange
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));

        // Act
        Optional<Patient> foundPatient = patientService.findPatient(1);

        // Assert
        assertThat(foundPatient).isPresent();
        assertThat(foundPatient.get()).isEqualTo(patient);
        verify(patientRepository).findById(1);
    }

    @Test
    void findPatient_shouldReturnEmpty_whenPatientDoesNotExist() {
        // Arrange
        when(patientRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        Optional<Patient> foundPatient = patientService.findPatient(1);

        // Assert
        assertThat(foundPatient).isNotPresent();
        verify(patientRepository).findById(1);
    }

    @Test
    void findAll_shouldReturnAllPatients() {
        // Arrange
        List<Patient> patients = List.of(patient);
        when(patientRepository.findAll()).thenReturn(patients);

        // Act
        Iterable<Patient> foundPatients = patientService.findAll();

        // Assert
        assertThat(foundPatients).containsExactly(patient);
        verify(patientRepository).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoPatientsExist() {
        // Arrange
        when(patientRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Iterable<Patient> foundPatients = patientService.findAll();

        // Assert
        assertThat(foundPatients).isEmpty();
        verify(patientRepository).findAll();
    }


    @Test
    void addPatient_shouldSaveAndReturnPatientModel() {
        // Arrange
        when(addressService.getOrCreateAddress(addressDto)).thenReturn(address);
        when(patientMapper.toEntity(patientDto)).thenReturn(patient); // Return patient without ID initially
        Patient savedPatient = new Patient(); // Simulate patient after saving (with ID)
        savedPatient.setId(1);
        savedPatient.setFirstName(patient.getFirstName());
        // ... copy other properties ...
        savedPatient.setAddress(address);
        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);
        when(patientAssembler.toModel(savedPatient)).thenReturn(patientModel);


        // Act
        PatientModel result = patientService.addPatient(patientDto);

        // Assert
        assertThat(result).isEqualTo(patientModel);
        verify(addressService).getOrCreateAddress(addressDto);
        verify(patientMapper).toEntity(patientDto);
        verify(patientRepository).save(patient); // Verify save is called with the mapped entity
        verify(patientAssembler).toModel(savedPatient); // Verify assembler is called with the saved entity
    }

    @Test
    void updatePatient_shouldUpdateAndReturnPatientModel_whenPatientExists() {
        // Arrange
        int patientId = 1;
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(addressService.getOrCreateAddress(addressDto)).thenReturn(address);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient); // Assume save returns the updated patient
        when(patientAssembler.toModel(patient)).thenReturn(patientModel);

        // Act
        PatientModel result = patientService.updatePatient(patientId, patientDto);

        // Assert
        assertThat(result).isEqualTo(patientModel);
        verify(patientRepository).findById(patientId);
        verify(patientMapper).updatePatientFromDto(patientDto, patient); // Verify update method was called
        verify(addressService).getOrCreateAddress(addressDto);
        verify(patientRepository).save(patient);
        verify(patientAssembler).toModel(patient);
    }

    @Test
    void updatePatient_shouldThrowNoSuchElementException_whenPatientDoesNotExist() {
        // Arrange
        int patientId = 99;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> patientService.updatePatient(patientId, patientDto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("This patient does not exist");

        verify(patientRepository).findById(patientId);
        verifyNoInteractions(patientMapper, addressService, patientAssembler);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void deletePatient_shouldReturnSuccess_whenDeletionSuccessful() {
        // Arrange
        int patientId = 1;
        doNothing().when(patientRepository).deleteById(patientId);
        when(patientRepository.existsById(patientId)).thenReturn(false); // Simulate successful deletion

        GenericResponseModel expectedResponse = new GenericResponseModel(true, String.format("Patient n°%s have been correctly deleted", patientId));

        // Act
        GenericResponseModel result = patientService.deletePatient(patientId);

        // Assert
        assertThat(result).isEqualTo(expectedResponse);
        verify(patientRepository).deleteById(patientId);
        verify(patientRepository).existsById(patientId);
    }

    @Test
    void deletePatient_shouldReturnError_whenDeletionFails() {
        // Arrange
        int patientId = 1;
        doNothing().when(patientRepository).deleteById(patientId);
        when(patientRepository.existsById(patientId)).thenReturn(true); // Simulate deletion failure

        GenericResponseModel expectedResponse = new GenericResponseModel(false, String.format("Error: Patient  n°%s have not been correctly deleted", patientId));

        // Act
        GenericResponseModel result = patientService.deletePatient(patientId);

        // Assert
        assertThat(result).isEqualTo(expectedResponse);
        verify(patientRepository).deleteById(patientId);
        verify(patientRepository).existsById(patientId);
    }
}