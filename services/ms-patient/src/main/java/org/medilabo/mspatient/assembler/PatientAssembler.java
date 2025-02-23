package org.medilabo.mspatient.assembler;

import org.medilabo.mspatient.entity.Patient;
import org.medilabo.mspatient.model.PatientModel;
import org.springframework.stereotype.Component;

@Component
public class PatientAssembler {

    public PatientModel toModel(Patient patient) {
        return new PatientModel(patient.getId(),patient.getLastName(), patient.getFirstName(), patient.getDateOfBirth(), patient.getGender(), patient.getAddress(), patient.getPhoneNumber());
    }
}
