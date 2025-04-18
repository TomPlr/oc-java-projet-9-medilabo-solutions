package org.medilabo.mspatient.controller;

import org.medilabo.mspatient.assembler.PatientAssembler;
import org.medilabo.mspatient.dto.PatientDto;
import org.medilabo.mspatient.model.GenericResponseModel;
import org.medilabo.mspatient.model.PatientModel;
import org.medilabo.mspatient.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final PatientAssembler patientAssembler;

    public PatientController(PatientService patientService, PatientAssembler patientAssembler) {
        this.patientService = patientService;
        this.patientAssembler = patientAssembler;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PatientModel>> getAllPatients() {
        List<PatientModel> patients = new ArrayList<>();

        patientService.findAll().forEach(patient -> patients.add(patientAssembler.toModel(patient)));

        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PatientModel> addPatient(@RequestBody PatientDto patient) {
        return new ResponseEntity<>(patientService.addPatient(patient), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientModel> getPatient(@PathVariable int id) {
        PatientModel patient = patientAssembler.toModel(patientService.findPatient(id).orElseThrow(() -> new NoSuchElementException("This patient does not exist")));

        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientModel> updatePatient(@PathVariable int id, @RequestBody PatientDto patient) {
        return new ResponseEntity<>(patientService.updatePatient(id, patient), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<GenericResponseModel> deletePatient(@RequestParam int id) {
        return new ResponseEntity<>(patientService.deletePatient(id), HttpStatus.OK);
    }
}
