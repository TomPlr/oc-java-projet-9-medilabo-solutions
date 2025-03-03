package org.medilabo.mspatient.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User {
    @Id
    private int id;

    @NotBlank(message = "The last name must be filled")
    private String lastName;

    @NotBlank(message = "The first name must be filled")
    private String firstName;

    @NotBlank(message = "The email must be filled")
    private String email;

    @NotBlank(message = "The password must be filled")
    private String password;

    @NotBlank(message = "The role must be filled")
    private String role;
}
