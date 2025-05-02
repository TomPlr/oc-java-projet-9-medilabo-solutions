import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Patient} from '../models/patient.model';
import {Note} from '../models/note.model';
import {Observable} from 'rxjs';
import {AssessmentRequestData, RiskLevel} from '../models/assessment.model';

@Injectable({
  providedIn: 'root'
})
export class AssessmentService {
  private http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/assessment';


  /**
   * Calculates the diabetes risk level for a patient based on their demographic information and medical notes
   *
   * @param patient - The patient object containing demographic information such as date of birth and gender
   * @param notes - An array of medical notes associated with the patient that will be analyzed for risk triggers
   * @returns An Observable that emits the calculated risk level (NONE, BORDERLINE, IN_DANGER, or EARLY_ONSET)
   */
  loadAssessment(patient: Patient, notes: Note[]): Observable<RiskLevel> {
    const assessmentRequestData: AssessmentRequestData = {
      dateOfBirth: patient.dateOfBirth,
      gender: patient.gender,
      notes: notes
    }

    return this.http.post<RiskLevel>(this.apiUrl, assessmentRequestData);
  }
}
