package org.medilabo.mspatient.service;

import org.medilabo.mspatient.dto.PatientDto;
import org.medilabo.mspatient.entity.Patient;
import org.medilabo.mspatient.model.GenericResponseModel;
import org.medilabo.mspatient.model.PatientModel;

import java.util.Optional;

public interface PatientService {

    Optional<Patient> findPatient(int id);

    Iterable<Patient> findAll();

    PatientModel addPatient(PatientDto patientDto);

    PatientModel updatePatient(int id, PatientDto patient);

    GenericResponseModel deletePatient(int id);
}
