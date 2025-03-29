package org.medilabo.mspatient.dto;

public record PatientDto(String lastName, String firstName, String dateOfBirth,
        String gender, AddressDto address,
        String phoneNumber) {
}
