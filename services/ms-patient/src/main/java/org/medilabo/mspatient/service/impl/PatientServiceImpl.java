package org.medilabo.mspatient.service.impl;

import org.medilabo.mspatient.assembler.PatientAssembler;
import org.medilabo.mspatient.dto.PatientDto;
import org.medilabo.mspatient.entity.Address;
import org.medilabo.mspatient.entity.Patient;
import org.medilabo.mspatient.mapper.PatientMapper;
import org.medilabo.mspatient.model.GenericResponseModel;
import org.medilabo.mspatient.model.PatientModel;
import org.medilabo.mspatient.repository.PatientRepository;
import org.medilabo.mspatient.service.AddressService;
import org.medilabo.mspatient.service.PatientService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientAssembler patientAssembler;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AddressService addressService;

    public PatientServiceImpl(PatientRepository patientRepository, PatientAssembler patientAssembler,
                              PatientMapper patientMapper, AddressService addressService) {
        this.patientRepository = patientRepository;
        this.patientAssembler = patientAssembler;
        this.patientMapper = patientMapper;
        this.addressService = addressService;
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
        Address address = addressService.getOrCreateAddress(patientDto.address());

        Patient patient = patientMapper.toEntity(patientDto);
        patient.setAddress(address);

        return patientAssembler.toModel(patientRepository.save(patient));
    }

    @Override
    public PatientModel updatePatient(int id, PatientDto patientDto) {
        // Find the existing patient
        Patient patientEntity = patientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("This patient does not exist"));

        patientMapper.updatePatientFromDto(patientDto, patientEntity);

        Address address = addressService.getOrCreateAddress(patientDto.address());
        patientEntity.setAddress(address);

        return patientAssembler.toModel(patientRepository.save(patientEntity));
    }

    @Override
    public GenericResponseModel deletePatient(int id) {
        patientRepository.deleteById(id);

        if (patientRepository.existsById(id)) {
            return new GenericResponseModel(false,
                    String.format("Error: Patient  n°%s have not been correctly deleted", id));
        }

        return new GenericResponseModel(true, String.format("Patient n°%s have been correctly deleted", id));
    }
}
