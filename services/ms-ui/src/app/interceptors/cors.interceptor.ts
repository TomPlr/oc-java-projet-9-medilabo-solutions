import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { ConfigService } from '../services/config.service';

export const corsInterceptor: HttpInterceptorFn = (req, next) => {
  const configService = inject(ConfigService);
  const keycloakConfig = configService.getKeycloakConfig();

  // Only modify Keycloak requests
  if (req.url.includes(keycloakConfig.url)) {
    const corsReq = req.clone({
      withCredentials: true
    });
    return next(corsReq);
  }

  return next(req);
}; 