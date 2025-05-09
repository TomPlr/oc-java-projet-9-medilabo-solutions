package org.medilabo.mspatient.dto;

import java.time.LocalDate;

public record PatientDto(String lastName, String firstName, LocalDate dateOfBirth,
                         String gender, AddressDto address,
                         String phoneNumber) {
}
