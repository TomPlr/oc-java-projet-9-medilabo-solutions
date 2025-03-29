package org.medilabo.mspatient.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressId;

    @NotBlank(message = "The street must be filled")
    private String street;
    @NotBlank(message = "The city must be fillet")
    private String city;
    @NotBlank(message = "The postal code must be fillet")
    private String postalCode;
}