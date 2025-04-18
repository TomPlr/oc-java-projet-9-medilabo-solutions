import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PatientsListComponent } from './patients/patients-list/patients-list.component';
import { PatientDetailComponent } from './patients/patient-detail/patient-detail.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: '',
    component: DashboardComponent,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        redirectTo: 'patients',
        pathMatch: 'full'
      },
      {
        path: 'patients',
        component: PatientsListComponent
      },
      {
        path: 'patients/:id',
        component: PatientDetailComponent
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
