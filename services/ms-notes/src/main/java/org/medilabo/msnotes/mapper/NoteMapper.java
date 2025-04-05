package org.medilabo.msnotes.mapper;

import org.mapstruct.Mapper;
import org.medilabo.msnotes.document.Note;
import org.medilabo.msnotes.dto.NoteDto;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    Note toEntity(NoteDto noteDto);
}
