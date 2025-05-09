package org.medilabo.msassessment.model;

public record PatientModel(int id, String lastName, String firstName, String dateOfBirth, String gender,
                           AddressModel address,
                           String phoneNumber) {
}

