package org.medilabo.mspatient.model;

public enum RoleModel {
    MANAGER("manager"),
    MEDICAL_STAFF("medical_staff");

    final String role;

    RoleModel(String role) {
        this.role = role;
    }
}