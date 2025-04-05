package org.medilabo.msnotes.repository;

import org.medilabo.msnotes.document.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findAllByPatId(int patId);
}
