package org.medilabo.mspatient.service.impl;

import org.medilabo.mspatient.dto.AddressDto;
import org.medilabo.mspatient.entity.Address;
import org.medilabo.mspatient.repository.AddressRepository;
import org.medilabo.mspatient.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address getOrCreateAddress(AddressDto addressDto) {
        // First try to find an existing address
        Optional<Address> existingAddress = addressRepository.findByStreetAndCityAndPostalCode(
                addressDto.street(),
                addressDto.city(),
                addressDto.postalCode());

        if (existingAddress.isPresent()) {
            return existingAddress.get();
        }

        // Create a new address entity manually to avoid Hibernate state issues
        Address newAddress = new Address();
        newAddress.setStreet(addressDto.street());
        newAddress.setCity(addressDto.city());
        newAddress.setPostalCode(addressDto.postalCode());

        return addressRepository.save(newAddress);
    }
}
