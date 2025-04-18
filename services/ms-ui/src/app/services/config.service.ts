import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  private config: any = {};

  constructor() {
    this.loadConfig();
  }

  private loadConfig(): void {
    this.config = {
      keycloak: environment.keycloak,
      api: environment.api
    };
  }

  get(key: string): any {
    return this.config[key];
  }

  getKeycloakConfig(): any {
    return this.config.keycloak;
  }

  getApiConfig(): any {
    return this.config.api;
  }
} 