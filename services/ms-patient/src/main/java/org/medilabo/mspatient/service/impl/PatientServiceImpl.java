package org.medilabo.mspatient.service.impl;

import org.medilabo.mspatient.assembler.PatientAssembler;
import org.medilabo.mspatient.dto.PatientDto;
import org.medilabo.mspatient.entity.Patient;
import org.medilabo.mspatient.model.GenericResponseModel;
import org.medilabo.mspatient.model.PatientModel;
import org.medilabo.mspatient.repository.PatientRepository;
import org.medilabo.mspatient.service.PatientService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientAssembler patientAssembler;
    PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository, PatientAssembler patientAssembler) {
        this.patientRepository = patientRepository;
        this.patientAssembler = patientAssembler;
    }

    @Override
    public Optional<Patient> findPatient(int id) {
        return patientRepository.findById(id);
    }

    @Override
    public Iterable<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public PatientModel addPatient(PatientDto patientDto) {
        Patient patientEntity = new Patient();

        patientEntity.setFirstName(patientDto.firstName());
        patientEntity.setLastName(patientDto.lastName());
        patientEntity.setDateOfBirth(patientDto.dateOfBirth());
        patientEntity.setGender(patientDto.gender());
        patientEntity.setAddress(patientDto.address());
        patientEntity.setPhoneNumber(patientDto.phoneNumber());


        return patientAssembler.toModel(patientRepository.save(patientEntity));
    }

    @Override
    public PatientModel updatePatient(int id, PatientDto patientDto) {
       Patient patientEntity = patientRepository.findById(id).orElseThrow(() -> new NoSuchElementException("This patient does not exist"));

       Optional.ofNullable(patientDto.firstName()).ifPresent(patientEntity::setFirstName);
       Optional.ofNullable(patientDto.lastName()).ifPresent(patientEntity::setLastName);
       Optional.ofNullable(patientDto.dateOfBirth()).ifPresent(patientEntity::setDateOfBirth);
       Optional.ofNullable(patientDto.gender()).ifPresent(patientEntity::setGender);
       Optional.ofNullable(patientDto.address()).ifPresent(patientEntity::setAddress);
       Optional.ofNullable(patientDto.phoneNumber()).ifPresent(patientEntity::setPhoneNumber);

        return patientAssembler.toModel(patientRepository.save(patientEntity));
    }

    @Override
    public GenericResponseModel deletePatient(int id) {
        patientRepository.deleteById(id);

        if (patientRepository.existsById(id)) {
            return new GenericResponseModel(false, String.format("Error: Patient  n°%s have not been correctly deleted", id));
        }

        return new GenericResponseModel(true, String.format("Patient n°%s have been correctly deleted", id));
    }
}
