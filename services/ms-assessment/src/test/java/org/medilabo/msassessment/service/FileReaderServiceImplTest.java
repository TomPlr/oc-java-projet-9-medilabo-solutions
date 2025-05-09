package org.medilabo.msassessment.service;

import org.junit.jupiter.api.Test;
import org.medilabo.msassessment.service.impl.FileReaderServiceImpl;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileReaderServiceImplTest {

    @Test
    void getTriggerTerms_shouldReadAllTermsFromFile() {
        // Arrange
        Resource triggerTermsResource = new ClassPathResource("trigger-terms.txt");
        FileReaderServiceImpl fileReaderService = new FileReaderServiceImpl(triggerTermsResource);
        List<String> expectedTerms = loadExpectedTerms(triggerTermsResource);

        // Act
        List<String> result = fileReaderService.getTriggerTerms();

        // Assert
        assertThat(result).isNotEmpty()
                .hasSize(expectedTerms.size())
                .containsExactlyElementsOf(expectedTerms);
    }

    @Test
    void getTriggerTerms_shouldReturnLowercaseTerms() {
        // Arrange
        Resource triggerTermsResource = new ClassPathResource("trigger-terms.txt");
        FileReaderServiceImpl fileReaderService = new FileReaderServiceImpl(triggerTermsResource);

        // Act
        List<String> result = fileReaderService.getTriggerTerms();

        // Assert
        assertThat(result).isNotEmpty()
                .allSatisfy(term -> assertThat(term).isEqualTo(term.toLowerCase()));
    }

    @Test
    void getTriggerTerms_shouldContainSpecificFrenchMedicalTerms() {
        // Arrange
        Resource triggerTermsResource = new ClassPathResource("trigger-terms.txt");
        FileReaderServiceImpl fileReaderService = new FileReaderServiceImpl(triggerTermsResource);

        // Act
        List<String> result = fileReaderService.getTriggerTerms();

        // Assert
        assertThat(result).isNotEmpty()
                .contains(
                        "hémoglobine a1c",
                        "microalbumine",
                        "taille",
                        "fumeur",
                        "fumeuse",
                        "anormal",
                        "cholestérol",
                        "vertiges"
                );
    }

    @Test
    void getTriggerTerms_shouldReturnEmptyList_whenIOExceptionOccurs() throws IOException {
        // Arrange
        Resource mockResource = mock(Resource.class);
        when(mockResource.getInputStream()).thenThrow(new IOException("Fichier non trouvé"));
        
        FileReaderServiceImpl fileReaderService = new FileReaderServiceImpl(mockResource);

        // Act
        List<String> result = fileReaderService.getTriggerTerms();

        // Assert
        assertThat(result).isEmpty();
    }

    /**
     * Helper method to load expected terms from the resource file
     */
    private List<String> loadExpectedTerms(Resource resource) {
        List<String> terms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                terms.add(line.toLowerCase());
            }
        } catch (IOException e) {
            // Log error or rethrow as a runtime exception
            throw new RuntimeException("Error reading trigger terms file", e);
        }
        return terms;
    }
}