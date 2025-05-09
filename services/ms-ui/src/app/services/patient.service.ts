import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Patient} from '../models/patient.model';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private http = inject(HttpClient);

  private readonly apiUrl = environment.api.gateway_url + '/patient';

  /**
   * Retrieves all patients from the database
   *
   * @returns An Observable containing an array of Patient objects
   */
  getAllPatients(): Observable<Patient[]> {
    return this.http.get<Patient[]>(`${this.apiUrl}/all`);
  }

  /**
   * Retrieves a specific patient by their ID
   *
   * @param id - The unique identifier of the patient
   * @returns An Observable containing the requested Patient object
   */
  getPatientById(id: Number): Observable<Patient> {
    return this.http.get<Patient>(`${this.apiUrl}/${id}`);
  }

  /**
   * Updates an existing patient's information
   *
   * @param id - The unique identifier of the patient to update
   * @param patient - The Patient object containing updated information
   * @returns An Observable containing the updated Patient object
   */
  updatePatient(id: Number, patient: Patient): Observable<Patient> {
    return this.http.put<Patient>(`${this.apiUrl}/${id}`, patient);
  }
}
