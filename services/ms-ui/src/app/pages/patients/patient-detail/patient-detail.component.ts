import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {PatientService} from '../../../services/patient.service';
import {CommentService} from '../../../services/comment.service';
import {Patient} from '../../../models/patient.model';
import {Note} from '../../../models/note.model';
import {AddressPipe} from '../../../pipes/address.pipe';
import {AssessmentService} from '../../../services/assessment.service';
import {forkJoin, of} from 'rxjs';
import {catchError, finalize} from 'rxjs/operators';
import {assessmentResults, RiskLevel} from '../../../models/assessment.model';

@Component({
  selector: 'app-patient-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, AddressPipe],
  templateUrl: './patient-detail.component.html',
  styleUrls: ['./patient-detail.component.css']
})
export class PatientDetailComponent implements OnInit {
  currentUser: any;
  patient: Patient | null = null;
  comments: Note[] = [];
  loading: boolean = false;
  error: string | null = null;
  assessmentResult: RiskLevel | null = null;
  isEditMode: boolean = false;
  patientForm: FormGroup;
  commentForm: FormGroup;
  protected readonly assessmentResults = assessmentResults;
  private route = inject(ActivatedRoute);
  private patientService = inject(PatientService);
  private commentService = inject(CommentService);
  private assessmentService = inject(AssessmentService);

  constructor(
    private router: Router,
    private fb: FormBuilder,
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
      this.loading = true;
      this.error = null;

      const patient$ = this.patientService.getPatientById(patientId).pipe(
        catchError(err => {
          console.error('Error loading patient:', err);
          this.error = 'Failed to load patient details.';
          return of(null);
        })
      );

      const comments$ = this.commentService.getCommentsByPatientId(patientId).pipe(
        catchError(err => {
          console.error('Error loading comments:', err);
          this.error = (this.error ? this.error + ' ' : '') + 'Failed to load comments.';
          return of([]);
        })
      );


      forkJoin([patient$, comments$])
        .pipe(
          finalize(() => {
            this.loading = false;
          })
        )
        .subscribe(([patient, comments]) => {
          this.patient = patient;
          this.comments = this.sortComments(comments);

          if (this.patient && this.comments) {
            console.log('Patient and comments loaded, calling assessment service...');
            this.assessmentService.loadAssessment(this.patient, this.comments).subscribe({
              next: (result) => {
                this.assessmentResult = result;
                console.log('Assessment Result:', this.assessmentResult);
              },
              error: (err) => {
                console.error('Error loading assessment:', err);
                this.error = (this.error ? this.error + ' ' : '') + 'Failed to load assessment.';
              }
            });

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
          } else {
            console.error('Cannot proceed with assessment as patient or comments failed to load.');
          }
        });

    } else {
      this.error = 'Patient ID not found in route parameters.';
    }
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

    const comment: Note = {
      patientId: this.patient.id,
      content: this.commentForm.get('content')?.value.trim(),
      createdBy: this.currentUser?.firstName + ' ' + this.currentUser?.lastName || ''
    };

    this.commentService.addComment(comment).subscribe({
      next: (comment) => {
        this.comments.unshift(comment);
        if (this.patient) {
          this.assessmentService.loadAssessment(this.patient, this.comments).subscribe({
            next: (result) => {
              this.assessmentResult = result;
            },
            error: (err) => {
              console.error('Error reloading assessment:', err);
            }
          });
        }
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
        // Re-fetch assessment after deleting a comment
        if (this.patient) {
          this.assessmentService.loadAssessment(this.patient, this.comments).subscribe({
            next: (result) => {
              this.assessmentResult = result;
            },
            error: (err) => {
              console.error('Error reloading assessment:', err);
            }
          });
        }
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

  getAssessmentBadgeClass(): string {
    const riskLevel = this.assessmentResult;
    const baseClasses = 'inline-block px-3 py-1 text-xs font-semibold rounded-full ';
    switch (riskLevel) {
      case "NONE":
        return baseClasses + 'bg-green-100 text-green-800';
      case "BORDERLINE":
        return baseClasses + 'bg-yellow-100 text-yellow-800';
      case "EARLY_ONSET":
        return baseClasses + 'bg-orange-100 text-orange-800';
      case "IN_DANGER":
        return baseClasses + 'bg-red-100 text-red-800';
      default:
        return baseClasses + 'bg-gray-100 text-gray-800';
    }
  }

  private sortComments(comments: Note[]): Note[] {
    return comments && comments.length > 0 ? comments.sort((a, b) => new Date(b.date || 0).getTime() - new Date(a.date || 0).getTime()) : [];
  }
}
