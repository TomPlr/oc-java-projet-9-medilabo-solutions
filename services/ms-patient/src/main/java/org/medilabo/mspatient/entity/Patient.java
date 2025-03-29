package org.medilabo.mspatient.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "The last name must be filled")
    private String lastName;

    @NotBlank(message = "The first name must be filled")
    private String firstName;

    @NotBlank(message = "The date of birth must be filled")
    private String dateOfBirth;

    @NotBlank(message = "The gender must be filled")
    private String gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    private String phoneNumber;
}
