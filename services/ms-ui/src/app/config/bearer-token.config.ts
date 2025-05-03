import {
  createInterceptorCondition,
  INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
  IncludeBearerTokenCondition
} from 'keycloak-angular';
import {environment} from '../../environments/environment';

const URL_PATTERN: RegExp = new RegExp(`^(${environment.api.gateway_url})(/.*)?$`, 'i');

export const provideBearerTokenConfig = () => {
  return {
    provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
    useValue: [
      createInterceptorCondition<IncludeBearerTokenCondition>({
        urlPattern: URL_PATTERN,
        bearerPrefix: 'Bearer',
      }),
    ],
  };
};
