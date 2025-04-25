import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Note} from '../models/note.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private readonly apiUrl = 'http://localhost:8080/note';

  constructor(private http: HttpClient) {
  }

  // Get all comments for a patient
  getCommentsByPatientId(patientId: Number): Observable<Note[]> {
    return this.http.get<Note[]>(`${this.apiUrl}/patient/${patientId}`);
  }

  // Add a new comment
  addComment(note: Note): Observable<Note> {
    return this.http.post<Note>(`${this.apiUrl}`, note);
  }

  // Delete a comment
  deleteComment(noteId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${noteId}`);
  }
}
