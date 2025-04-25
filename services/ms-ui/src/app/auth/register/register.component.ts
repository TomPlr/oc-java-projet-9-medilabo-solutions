import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RegisterCredentials } from '../../models/auth/register-credentials.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  credentials: RegisterCredentials = {
    username: '',
    email: '',
    firstName: '',
    lastName: '',
    password: ''
  };

  confirmPassword: string = '';
  errorMessage: string = '';
  isLoading: boolean = false;
  passwordMismatch: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.passwordMismatch = this.credentials.password !== this.confirmPassword;

    if (this.passwordMismatch) {
      this.errorMessage = 'Passwords do not match';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.register(this.credentials).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response) {
          // Registration successful, redirect to login
          this.router.navigate(['/login']);
        } else {
          this.errorMessage = 'Registration failed. Please try again.';
        }
      },
      error: (error) => {
        ;
        this.isLoading = false;
        console.error('Registration error:', error);
        this.errorMessage = 'An error occurred during registration. Please try again.';
      }
    });
  }
}
