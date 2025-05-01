import {
  createInterceptorCondition,
  INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
  IncludeBearerTokenCondition
} from 'keycloak-angular';

const URL_PATTERN = /^(http:\/\/localhost:8080)(\/.*)?$/i;

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

