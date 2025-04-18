import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { tap, catchError, switchMap } from 'rxjs/operators';
import { User } from '../models/auth/user.model';
import { LoginCredentials } from '../models/auth/login-credentials.model';
import { RegisterCredentials } from '../models/auth/register-credentials.model';
import { ConfigService } from './config.service';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly keycloakConfig: any;
  private readonly keycloakUrl: string;
  private readonly realm: string;
  private readonly clientId: string;
  private readonly clientSecret: string;
  private readonly redirectUri: string;
  
  private tokenSubject = new BehaviorSubject<string | null>(null);
  public token$ = this.tokenSubject.asObservable();

  private refreshTokenTimeout: any;

  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {
    this.keycloakConfig = this.configService.getKeycloakConfig();
    
    this.keycloakUrl = this.keycloakConfig.url;
    this.realm = this.keycloakConfig.realm;
    this.clientId = this.keycloakConfig.clientId;
    this.clientSecret = this.keycloakConfig.clientSecret;
    this.redirectUri = this.keycloakConfig.redirectUri;

    this.initializeAuth();
  }

  private initializeAuth(): void {
    const token = localStorage.getItem('token');
    const refreshToken = localStorage.getItem('refresh_token');
    
    if (token) {
      this.tokenSubject.next(token);
      this.startRefreshTokenTimer();

      this.getUserInfo();
    }
  }

  login(credentials: LoginCredentials): Observable<any> {
    const body = new URLSearchParams();
    body.set('client_id', this.clientId);
    body.set('client_secret', this.clientSecret);
    body.set('grant_type', 'password');
    body.set('username', credentials.username);
    body.set('password', credentials.password);
    
    return this.http.post<any>(
      `${this.keycloakUrl}/realms/${this.realm}/protocol/openid-connect/token`,
      body.toString(),
      { 
        headers: { 
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      }
    ).pipe(
      tap(response => {
        this.handleTokenResponse(response);
      }),
      catchError(error => {
        console.error('Login error:', error);
        return throwError(() => new Error('Invalid credentials'));
      })
    );
  }

  register(credentials: RegisterCredentials): Observable<any> {
    const body = {
      username: credentials.username,
      email: credentials.email,
      firstName: credentials.firstName,
      lastName: credentials.lastName,
      enabled: true,
      credentials: [{
        type: 'password',
        value: credentials.password,
        temporary: false
      }]
    };

    return this.http.post(
      `${this.keycloakUrl}/admin/realms/${this.realm}/users`,
      body
    ).pipe(
      catchError(error => {
        console.error('Registration error:', error);
        return throwError(() => new Error('Registration failed'));
      })
    );
  }

  logout(): void {
    const refreshToken = localStorage.getItem('refresh_token');
    if (refreshToken) {
      // Revoke refresh token
      const body = new URLSearchParams();
      body.set('client_id', this.clientId);
      body.set('client_secret', this.clientSecret);
      body.set('refresh_token', refreshToken);
      
      this.http.post(
        `${this.keycloakUrl}/realms/${this.realm}/protocol/openid-connect/logout`,
        body.toString(),
        { 
          headers: { 
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        }
      ).subscribe();
    }

    this.stopRefreshTokenTimer();
    localStorage.removeItem('token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('user');
    
    // Redirect to Keycloak logout endpoint
    const logoutUrl = `${this.keycloakUrl}/realms/${this.realm}/protocol/openid-connect/logout`;
    const params = new URLSearchParams({
      client_id: this.clientId,
      redirect_uri: this.redirectUri
    });
    window.location.href = `${logoutUrl}?${params.toString()}`;
  }

  refreshToken(): Observable<any> {
    const refreshToken = localStorage.getItem('refresh_token');
    if (!refreshToken) {
      return throwError(() => new Error('No refresh token available'));
    }

    const body = new URLSearchParams();
    body.set('client_id', this.clientId);
    body.set('client_secret', this.clientSecret);
    body.set('grant_type', 'refresh_token');
    body.set('refresh_token', refreshToken);

    return this.http.post<any>(
      `${this.keycloakUrl}/realms/${this.realm}/protocol/openid-connect/token`,
      body.toString(),
      { 
        headers: { 
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      }
    ).pipe(
      tap(response => {
        this.handleTokenResponse(response);
      }),
      catchError(error => {
        console.error('Token refresh error:', error);
        this.logout();
        return throwError(() => new Error('Token refresh failed'));
      })
    );
  }

  private handleTokenResponse(response: any): void {
    localStorage.setItem('token', response.access_token);
    localStorage.setItem('refresh_token', response.refresh_token);
    this.tokenSubject.next(response.access_token);
    this.startRefreshTokenTimer(response.expires_in);
  }

  private startRefreshTokenTimer(expiresIn: number = 300): void {
    this.stopRefreshTokenTimer();
    // Refresh token 1 minute before expiration
    const timeout = (expiresIn - 60) * 1000;
    this.refreshTokenTimeout = setTimeout(() => {
      this.refreshToken().subscribe();
    }, timeout);
  }

  private stopRefreshTokenTimer(): void {
    if (this.refreshTokenTimeout) {
      clearTimeout(this.refreshTokenTimeout);
      this.refreshTokenTimeout = null;
    }
  }

  isAuthenticated(): boolean {
    return !!this.tokenSubject.value && !this.isTokenExpired();
  }

  getToken(): string | null {
    if (this.isTokenExpired()) {
      this.refreshToken().subscribe();
      return null;
    }
    return this.tokenSubject.value;
  }

  private isTokenExpired(): boolean {
    const token = this.tokenSubject.value;
    if (!token) return true;

    try {
      const payload = jwtDecode(token);
      const expiry = payload.exp! * 1000;
      return Date.now() >= expiry;
    } catch (error) {
      console.error('Error checking token expiration:', error);
      return true;
    }
  }

  private getUserInfo(): void {
    const token = this.getToken();
    if (token) {
      try {
        const payload = jwtDecode(token) as any;
        const user: User = {
          id: payload.sub,
          username: payload.preferred_username,
          email: payload.email,
          firstName: payload.given_name,
          lastName: payload.family_name,
          roles: payload.resource_access?.["medilabo-auth"]?.roles || []
        };
        localStorage.setItem('user', JSON.stringify(user));
      } catch (error) {
        console.error('Error decoding token:', error);
        localStorage.removeItem('user');
      }
    }
  }
} 