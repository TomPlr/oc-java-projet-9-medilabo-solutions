package org.medilabo.mspatient.service;

import org.medilabo.mspatient.dto.AddressDto;
import org.medilabo.mspatient.entity.Address;

public interface AddressService {

    Address getOrCreateAddress(AddressDto addressDto);

}
