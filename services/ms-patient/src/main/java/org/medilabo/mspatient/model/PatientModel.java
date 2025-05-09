package org.medilabo.mspatient.model;

import org.medilabo.mspatient.entity.Address;

public record PatientModel(int id, String lastName, String firstName, String dateOfBirth, String gender,
                           Address address,
                           String phoneNumber) {
}
