package org.medilabo.msnotes.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "note")
public class Note {

    @Id
    String id;

    int patientId;
    String content;
    Date date;
    String createdBy;
}
