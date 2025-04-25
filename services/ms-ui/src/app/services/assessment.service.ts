import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Patient } from '../models/patient.model';
import { Note } from '../models/note.model';
import { Observable } from 'rxjs';
import {AssessmentRequestData} from '../models/assessment.model';

@Injectable({
  providedIn: 'root'
})
export class AssessmentService {
  private readonly apiUrl = 'http://localhost:8080/assessment';

  constructor(private http: HttpClient) { }

  loadAssessment(patient: Patient, notes: Note[]): Observable<any> {

    const assessmentRequestData : AssessmentRequestData = {
      dateOfBirth: patient.dateOfBirth || "",
      gender: patient.gender || "",
      notes: notes
    }

    return this.http.post<any>(this.apiUrl, assessmentRequestData);
  }
}
