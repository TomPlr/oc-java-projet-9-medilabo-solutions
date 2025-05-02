package org.medilabo.msnotes.mapper;

import org.mapstruct.Mapper;
import org.medilabo.msnotes.document.Note;
import org.medilabo.msnotes.dto.NoteDto;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    /**
     * Converts a NoteDto to a Note entity.
     *
     * @param noteDto the DTO containing note data
     * @return the corresponding Note entity
     */
    Note toEntity(NoteDto noteDto);
}