package org.medilabo.mspatient.model;

import jakarta.validation.constraints.NotBlank;

public record PatientModel(int id, String lastName, String firstName, String dateOfBirth, String gender, String address,
                           String phoneNumber) {
}
