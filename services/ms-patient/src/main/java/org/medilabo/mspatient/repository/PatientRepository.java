package org.medilabo.mspatient.repository;

import org.medilabo.mspatient.entity.Patient;
import org.springframework.data.repository.CrudRepository;

public interface PatientRepository extends CrudRepository<Patient,Integer> {
}
