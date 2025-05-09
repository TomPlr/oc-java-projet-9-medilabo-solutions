package org.medilabo.mspatient.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.mspatient.dto.AddressDto;
import org.medilabo.mspatient.entity.Address;
import org.medilabo.mspatient.repository.AddressRepository;
import org.medilabo.mspatient.service.impl.AddressServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Address existingAddress;
    private AddressDto addressDto;

    @BeforeEach
    void setUp() {
        // Create test data
        addressDto = new AddressDto("123 Main St", "Test City", "12345");

        existingAddress = new Address();
        existingAddress.setAddressId(1);
        existingAddress.setStreet("123 Main St");
        existingAddress.setCity("Test City");
        existingAddress.setPostalCode("12345");
    }

    @Test
    void getOrCreateAddress_shouldReturnExistingAddress_whenAddressExists() {
        // Arrange
        when(addressRepository.findByStreetAndCityAndPostalCode(
                addressDto.street(),
                addressDto.city(),
                addressDto.postalCode()
        )).thenReturn(Optional.of(existingAddress));

        // Act
        Address result = addressService.getOrCreateAddress(addressDto);

        // Assert
        assertThat(result).isEqualTo(existingAddress);
        verify(addressRepository).findByStreetAndCityAndPostalCode(
                addressDto.street(),
                addressDto.city(),
                addressDto.postalCode()
        );
        verifyNoMoreInteractions(addressRepository);
    }

    @Test
    void getOrCreateAddress_shouldCreateAndSaveNewAddress_whenAddressDoesNotExist() {
        // Arrange
        when(addressRepository.findByStreetAndCityAndPostalCode(
                addressDto.street(),
                addressDto.city(),
                addressDto.postalCode()
        )).thenReturn(Optional.empty());

        Address savedAddress = new Address();
        savedAddress.setAddressId(1);
        savedAddress.setStreet(addressDto.street());
        savedAddress.setCity(addressDto.city());
        savedAddress.setPostalCode(addressDto.postalCode());

        when(addressRepository.save(any(Address.class))).thenReturn(savedAddress);

        // Act
        Address result = addressService.getOrCreateAddress(addressDto);

        // Assert
        assertThat(result).isEqualTo(savedAddress);
        assertThat(result.getStreet()).isEqualTo(addressDto.street());
        assertThat(result.getCity()).isEqualTo(addressDto.city());
        assertThat(result.getPostalCode()).isEqualTo(addressDto.postalCode());

        verify(addressRepository).findByStreetAndCityAndPostalCode(
                addressDto.street(),
                addressDto.city(),
                addressDto.postalCode()
        );
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void getOrCreateAddress_shouldPreserveAllAddressFields_whenCreatingNewAddress() {
        // Arrange
        AddressDto complexAddressDto = new AddressDto("456 Oak Avenue", "New York", "10001");

        when(addressRepository.findByStreetAndCityAndPostalCode(
                complexAddressDto.street(),
                complexAddressDto.city(),
                complexAddressDto.postalCode()
        )).thenReturn(Optional.empty());

        // Capture the address being saved
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> {
            Address savedAddress = invocation.getArgument(0);
            savedAddress.setAddressId(2); // Simulate DB assigning ID
            return savedAddress;
        });

        // Act
        Address result = addressService.getOrCreateAddress(complexAddressDto);

        // Assert
        assertThat(result.getAddressId()).isEqualTo(2);
        assertThat(result.getStreet()).isEqualTo(complexAddressDto.street());
        assertThat(result.getCity()).isEqualTo(complexAddressDto.city());
        assertThat(result.getPostalCode()).isEqualTo(complexAddressDto.postalCode());

        verify(addressRepository).findByStreetAndCityAndPostalCode(
                complexAddressDto.street(),
                complexAddressDto.city(),
                complexAddressDto.postalCode()
        );
        verify(addressRepository).save(any(Address.class));
    }
}