package org.medilabo.msnotes.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "note")
public class Note {

    @Id
    public String id;

    public int patId;
    public String patient;
    public String content;

}
