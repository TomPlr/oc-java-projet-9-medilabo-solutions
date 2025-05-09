import {Routes} from '@angular/router';
import {DashboardComponent} from './pages/dashboard/dashboard.component';
import {PatientsListComponent} from './pages/patients/patients-list/patients-list.component';
import {PatientDetailComponent} from './pages/patients/patient-detail/patient-detail.component';
import {canActivateAuthRole} from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    canActivate: [canActivateAuthRole],
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
