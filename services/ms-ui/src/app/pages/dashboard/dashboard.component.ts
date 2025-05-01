import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import Keycloak, {KeycloakProfile} from 'keycloak-js';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  currentUser: KeycloakProfile | null = null;
  private keycloak = inject(Keycloak);

  ngOnInit(): void {
    this.keycloak.loadUserProfile().then(user => {
      localStorage.setItem('user', JSON.stringify(user));
      this.currentUser = JSON.parse(localStorage.getItem('user') || '{}');
    })
  }


  logout(): void {
    this.keycloak.logout();
  }
}
