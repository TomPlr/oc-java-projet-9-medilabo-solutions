package org.medilabo.msassessment.service;

import java.util.List;


public interface FileReaderService {

    /**
     * Retrieves a list of medical trigger terms from the configured resource file.
     * These terms are used to identify potential risk factors in patient notes.
     *
     * @return A list of trigger terms in lowercase format
     */
    List<String> getTriggerTerms();
}