import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {PatientService} from '../../../services/patient.service';
import {Patient} from '../../../models/patient.model';
import {Note} from '../../../models/note.model';
import {AddressPipe} from '../../../pipes/address.pipe';
import {AssessmentService} from '../../../services/assessment.service';
import {forkJoin, of} from 'rxjs';
import {catchError, finalize} from 'rxjs/operators';
import {assessmentResults, RiskLevel} from '../../../models/assessment.model';
import {NoteService} from '../../../services/note.service';

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
  notes: Note[] = [];
  loading: boolean = false;
  error: string | null = null;
  assessmentResult: RiskLevel | null = null;
  isEditMode: boolean = false;
  patientForm: FormGroup;
  noteForm: FormGroup;
  protected readonly assessmentResults = assessmentResults;
  private route = inject(ActivatedRoute);
  private patientService = inject(PatientService);
  private noteService = inject(NoteService);
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

    this.noteForm = this.fb.group({
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

      const comments$ = this.noteService.getNotesByPatientId(patientId).pipe(
        catchError(err => {
          console.error('Error loading notes:', err);
          this.error = (this.error ? this.error + ' ' : '') + 'Failed to load notes.';
          return of([]);
        })
      );


      forkJoin([patient$, comments$])
        .pipe(
          finalize(() => {
            this.loading = false;
          })
        )
        .subscribe(([patient, notes]) => {
          this.patient = patient;
          this.notes = this.sortComments(notes);

          if (this.patient && this.notes) {
            console.log('Patient and notes loaded, calling assessment service...');
            this.assessmentService.loadAssessment(this.patient, this.notes).subscribe({
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
            console.error('Cannot proceed with assessment as patient or notes failed to load.');
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

  addNote(): void {
    if (!this.patient?.id || this.noteForm.invalid) return;

    const note: Note = {
      patientId: this.patient.id,
      content: this.noteForm.get('content')?.value.trim(),
      createdBy: this.currentUser?.firstName + ' ' + this.currentUser?.lastName || ''
    };

    this.noteService.addNote(note).subscribe({
      next: (note) => {
        this.notes.unshift(note);
        if (this.patient) {
          this.assessmentService.loadAssessment(this.patient, this.notes).subscribe({
            next: (result) => {
              this.assessmentResult = result;
            },
            error: (err) => {
              console.error('Error reloading assessment:', err);
            }
          });
        }
        this.noteForm.reset();
      },
      error: (err) => {
        this.error = 'Failed to add note. Please try again later.';
        console.error('Error adding note:', err);
      }
    });
  }

  clearNote(): void {
    this.noteForm.reset();
  }

  deleteNote(noteId: number): void {
    this.noteService.deleteNote(noteId).subscribe({
      next: () => {
        this.notes = this.notes.filter(comment => comment.id !== noteId);
        // Re-fetch assessment after deleting a comment
        if (this.patient) {
          this.assessmentService.loadAssessment(this.patient, this.notes).subscribe({
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

  private sortComments(notes: Note[]): Note[] {
    return notes && notes.length > 0 ? notes.sort((a, b) => new Date(b.date || 0).getTime() - new Date(a.date || 0).getTime()) : [];
  }
}
