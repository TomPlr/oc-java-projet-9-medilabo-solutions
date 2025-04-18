import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PatientService } from '../../services/patient.service';
import { AuthService } from '../../services/auth.service';
import { Patient } from '../../models/patient.model';
import { User } from '../../models/auth/user.model';
import { AddressPipe } from '../../pipes/address.pipe';

@Component({
  selector: 'app-patient-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, AddressPipe],
  templateUrl: './patient-detail.component.html',
  styleUrls: ['./patient-detail.component.css']
})
export class PatientDetailComponent implements OnInit {
  patient: Patient | null = null;
  currentUser: User | null = null;
  newComment: string = '';
  loading: boolean = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private patientService: PatientService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });

    const patientId = this.route.snapshot.paramMap.get('id');
    if (patientId) {
      this.loadPatient(patientId);
    } else {
      this.error = 'Patient ID not found';
      this.loading = false;
    }
  }

  private loadPatient(id: string): void {
    this.loading = true;
    this.error = null;
    
    this.patientService.getPatientById(id).subscribe({
      next: (patient) => {
        this.patient = patient;
   
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load patient data. Please try again later.';
        this.loading = false;
        console.error('Error loading patient:', err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/patients']);
  }
} 