package org.medilabo.mspatient.service;

import org.medilabo.mspatient.dto.PatientDto;
import org.medilabo.mspatient.entity.Patient;
import org.medilabo.mspatient.model.GenericResponseModel;
import org.medilabo.mspatient.model.PatientModel;

import java.util.Optional;

public interface PatientService {

    public Optional<Patient> findPatient(int id);

    public Iterable<Patient> findAll();

    public PatientModel addPatient(PatientDto patientDto);

    public PatientModel updatePatient(int id,PatientDto patient);

    public GenericResponseModel deletePatient(int id);
}
