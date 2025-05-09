package org.medilabo.mspatient.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.medilabo.mspatient.dto.AddressDto;
import org.medilabo.mspatient.entity.Address;
import org.medilabo.mspatient.model.AddressModel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    AddressDto toDto(Address address);

    Address toEntity(AddressDto addressDto);

    AddressModel toModel(Address address);
}
