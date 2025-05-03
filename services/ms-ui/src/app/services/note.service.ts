import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Note} from '../models/note.model';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NoteService {
  private http = inject(HttpClient);

  private readonly apiUrl = environment.api.gateway_url + '/note';

  /**
   * Retrieves all medical notes for a specific patient
   *
   * @param patientId - The unique identifier of the patient
   * @returns An Observable containing an array of Note objects
   */
  getNotesByPatientId(patientId: Number): Observable<Note[]> {
    return this.http.get<Note[]>(`${this.apiUrl}/patient/${patientId}`);
  }

  /**
   * Creates a new medical note
   *
   * @param note - The Note object to be created
   * @returns An Observable containing the created Note object with assigned ID
   */
  addNote(note: Note): Observable<Note> {
    return this.http.post<Note>(`${this.apiUrl}`, note);
  }

  /**
   * Deletes a specific medical note
   *
   * @param noteId - The unique identifier of the note to delete
   * @returns An Observable that completes when the deletion is successful
   */
  deleteNote(noteId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${noteId}`);
  }
}
