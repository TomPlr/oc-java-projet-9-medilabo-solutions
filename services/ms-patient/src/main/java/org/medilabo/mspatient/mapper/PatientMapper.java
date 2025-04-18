package org.medilabo.mspatient.mapper;

import org.mapstruct.*;
import org.medilabo.mspatient.dto.PatientDto;
import org.medilabo.mspatient.entity.Patient;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientMapper {

    Patient toEntity(PatientDto patientDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    void updatePatientFromDto(PatientDto dto, @MappingTarget Patient patient);
}
