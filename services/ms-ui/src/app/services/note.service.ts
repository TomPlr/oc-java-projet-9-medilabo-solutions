import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Note} from '../models/note.model';

@Injectable({
  providedIn: 'root'
})
export class NoteService {
  private readonly apiUrl = 'http://localhost:8080/note';

  constructor(private http: HttpClient) {
  }

  // Get all notes for a patient
  getNotesByPatientId(patientId: Number): Observable<Note[]> {
    return this.http.get<Note[]>(`${this.apiUrl}/patient/${patientId}`);
  }

  // Add a new note
  addNote(note: Note): Observable<Note> {
    return this.http.post<Note>(`${this.apiUrl}`, note);
  }

  // Delete a note
  deleteNote(noteId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${noteId}`);
  }
}
