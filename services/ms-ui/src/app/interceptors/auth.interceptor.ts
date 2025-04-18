import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { catchError, switchMap } from 'rxjs/operators';
import { Observable, of, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  
  console.log('Intercepting request to:', req.url);
  
  // Skip adding token for Keycloak endpoints
  if (req.url.includes('protocol/openid-connect')) {
    console.log('Skipping auth for Keycloak endpoint');
    return next(req);
  }

  const token = authService.getToken();
  console.log('Token available:', !!token);
  
  if (token) {
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    
    console.log('Added auth header:', `Bearer ${token.substring(0, 20)}...`);
    
    return next(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          console.log('Received 401, attempting token refresh');
          // Token might be expired, try to refresh
          return authService.refreshToken().pipe(
            switchMap(() => {
              const newToken = authService.getToken();
              if (newToken) {
                console.log('Token refreshed, retrying with new token');
                const newAuthReq = req.clone({
                  headers: req.headers.set('Authorization', `Bearer ${newToken}`)
                });
                return next(newAuthReq);
              }
              return throwError(() => error);
            }),
            catchError(() => {
              console.log('Token refresh failed, logging out');
              // If refresh fails, redirect to login
              authService.logout();
              return throwError(() => error);
            })
          );
        }
        return throwError(() => error);
      })
    );
  }
  
  console.log('No token available, sending request without auth');
  return next(req);
}; 