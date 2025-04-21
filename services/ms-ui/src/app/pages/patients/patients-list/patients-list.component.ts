import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PatientService } from '../../../services/patient.service';
import { Patient } from '../../../models/patient.model';
import { AddressPipe } from '../../../pipes/address.pipe';

@Component({
  selector: 'app-patients-list',
  standalone: true,
  imports: [CommonModule, RouterModule, AddressPipe],
  templateUrl: './patients-list.component.html',
  styleUrls: ['./patients-list.component.css']
})
export class PatientsListComponent implements OnInit {
  patients: Patient[] = [];
  loading = false;
  error: string | null = null;

  constructor(private readonly patientService: PatientService) { }

  ngOnInit(): void {
    this.loadPatients();
  }

  loadPatients(): void {
    this.loading = true;
    this.error = null;

    this.patientService.getAllPatients().subscribe({
      next: (data) => {
        this.patients = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error loading patients. Please try again later.';
        this.loading = false;
        console.error('Error fetching patients:', err);
      }
    });
  }
}
