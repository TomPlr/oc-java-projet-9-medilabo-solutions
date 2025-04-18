import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private readonly apiUrl = 'http://localhost:8080/note';

  constructor(private http: HttpClient) { }

  // Get all comments for a patient
  getCommentsByPatientId(patientId: Number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/patient/${patientId}`);
  }

  // Add a new comment
  addComment(comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(`${this.apiUrl}`, comment);
  }

  // Delete a comment
  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${commentId}`);
  }
} 