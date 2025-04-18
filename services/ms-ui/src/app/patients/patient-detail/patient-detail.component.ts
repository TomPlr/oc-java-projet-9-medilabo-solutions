import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PatientService } from '../../services/patient.service';
import { CommentService } from '../../services/comment.service';
import { Patient } from '../../models/patient.model';
import { Comment } from '../../models/comment.model';
import { User } from '../../models/auth/user.model';
import { AddressPipe } from '../../pipes/address.pipe';

@Component({
  selector: 'app-patient-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, AddressPipe],
  templateUrl: './patient-detail.component.html',
  styleUrls: ['./patient-detail.component.css']
})
export class PatientDetailComponent implements OnInit {
  patient: Patient | null = null;
  currentUser: User | null = null;
  comments: Comment[] = [];
  loading: boolean = true;
  error: string | null = null;
  isEditMode: boolean = false;
  patientForm: FormGroup;
  commentForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private patientService: PatientService,
    private commentService: CommentService,
    private fb: FormBuilder
  ) {
    this.patientForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      gender: ['', Validators.required],
      address: this.fb.group({
        street: [''],
        city: [''],
        postalCode: ['']
      }),
      phoneNumber: ['']
    });

    this.commentForm = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(1)]]
    });
  }

  ngOnInit(): void {
    this.currentUser = JSON.parse(localStorage.getItem('user') || '{}');

    const patientId = Number(this.route.snapshot.paramMap.get('patientId'));
    if (patientId) {
      this.loadPatient(patientId);
      this.loadComments(patientId);
    } else {
      this.error = 'Patient ID not found';
      this.loading = false;
    }
  }

  private loadPatient(id: Number): void {
    this.loading = true;
    this.error = null;
    
    this.patientService.getPatientById(id).subscribe({
      next: (patient) => {
        this.patient = patient;
        this.patientForm.patchValue({
          firstName: patient.firstName,
          lastName: patient.lastName,
          dateOfBirth: patient.dateOfBirth,
          gender: patient.gender,
          address: patient.address || {
            street: '',
            city: '',
            postalCode: ''
          },
          phoneNumber: patient.phoneNumber
        });
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load patient data. Please try again later.';
        this.loading = false;
        console.error('Error loading patient:', err);
      }
    });
  }

  private loadComments(patientId: Number): void {
    this.commentService.getCommentsByPatientId(patientId).subscribe({
      next: (comments) => {
        this.comments = comments.sort((a, b) => {
          return new Date(b.date || 0).getTime() - new Date(a.date || 0).getTime();
        });
      },
      error: (err) => {
        console.error('Error loading comments:', err);
      }
    });
  }

  toggleEditMode(): void {
    this.isEditMode = !this.isEditMode;
    if (!this.isEditMode) {
      this.patientForm.reset();
      if (this.patient) {
        this.patientForm.patchValue({
          firstName: this.patient.firstName,
          lastName: this.patient.lastName,
          dateOfBirth: this.patient.dateOfBirth,
          gender: this.patient.gender,
          address: this.patient.address || {
            street: '',
            city: '',
            postalCode: ''
          },
          phoneNumber: this.patient.phoneNumber
        });
      }
    }
  }

  updatePatient(): void {
    if (!this.patient?.id || this.patientForm.invalid) return;

    this.loading = true;
    this.error = null;

    const updatedPatient: Patient = {
      ...this.patient,
      ...this.patientForm.value
    };
    debugger;
    this.patientService.updatePatient(this.patient.id, updatedPatient).subscribe({
      next: (patient) => {
        this.patient = patient;
        this.isEditMode = false;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to update patient information. Please try again later.';
        this.loading = false;
        console.error('Error updating patient:', err);
      }
    });
  }

  cancelEdit(): void {
    this.isEditMode = false;
    if (this.patient) {
      this.patientForm.patchValue({
        firstName: this.patient.firstName,
        lastName: this.patient.lastName,
        dateOfBirth: this.patient.dateOfBirth,
        gender: this.patient.gender,
        address: this.patient.address || {
          street: '',
          city: '',
          postalCode: ''
        },
        phoneNumber: this.patient.phoneNumber
      });
    }
  }

  addComment(): void {
    if (!this.patient?.id || this.commentForm.invalid) return;

    const comment: Comment = {
      patientId: this.patient.id,
      content: this.commentForm.get('content')?.value.trim(),
      createdBy: this.currentUser?.firstName + ' ' + this.currentUser?.lastName || ''
    };

    this.commentService.addComment(comment).subscribe({
      next: (comment) => {
        this.comments.unshift(comment);
        this.commentForm.reset();
      },
      error: (err) => {
        this.error = 'Failed to add comment. Please try again later.';
        console.error('Error adding comment:', err);
      }
    });
  }

  clearComment(): void {
    this.commentForm.reset();
  }

  deleteComment(commentId: number): void {
    this.commentService.deleteComment(commentId).subscribe({
      next: () => {
        this.comments = this.comments.filter(comment => comment.id !== commentId);
      },
      error: (err) => {
        this.error = 'Failed to delete comment. Please try again later.';
        console.error('Error deleting comment:', err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/patients']);
  }
} 