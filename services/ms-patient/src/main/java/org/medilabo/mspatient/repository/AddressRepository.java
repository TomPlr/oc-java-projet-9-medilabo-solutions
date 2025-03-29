package org.medilabo.mspatient.repository;

import org.medilabo.mspatient.entity.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {

    Optional<Address> findByStreetAndCityAndPostalCode(String street, String city, String postalCode);
}
