package org.medilabo.mspatient.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.medilabo.mspatient.dto.PatientDto;
import org.medilabo.mspatient.entity.Patient;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientMapper {

    Patient toEntity(PatientDto patientDto);
}
