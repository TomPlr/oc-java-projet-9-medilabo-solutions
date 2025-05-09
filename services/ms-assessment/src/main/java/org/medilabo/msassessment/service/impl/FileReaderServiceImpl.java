package org.medilabo.msassessment.service.impl;

import org.medilabo.msassessment.service.FileReaderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileReaderServiceImpl implements FileReaderService {

    private final Resource triggerTermsResource;

    public FileReaderServiceImpl(@Value("classpath:trigger-terms.txt") Resource triggerTermsResource) {
        this.triggerTermsResource = triggerTermsResource;
    }

    @Override
    public List<String> getTriggerTerms() {
        List<String> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(triggerTermsResource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line.toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error reading trigger terms file: " + e.getMessage());
        }
        return result;
    }
}