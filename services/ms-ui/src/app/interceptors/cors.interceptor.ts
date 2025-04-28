import {HttpInterceptorFn} from '@angular/common/http';
import {environment} from '../../environments/environment';

export const corsInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloakConfig = environment.keycloak;

  // Only modify Keycloak requests
  if (req.url.includes(keycloakConfig.url)) {
    const corsReq = req.clone({
      withCredentials: true
    });
    return next(corsReq);
  }

  return next(req);
};
