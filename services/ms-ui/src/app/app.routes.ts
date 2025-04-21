import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { PatientsListComponent } from './pages/patients/patients-list/patients-list.component';
import { PatientDetailComponent } from './pages/patients/patient-detail/patient-detail.component';
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
        path: 'patients/:patientId',
        component: PatientDetailComponent
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
