export const environment = {
  production: false,
  keycloak: {
    url: import.meta.env.NG_APP_KEYCLOAK_URL,
    realm: import.meta.env.NG_APP_KEYCLOAK_REALM,
    clientId: import.meta.env.NG_APP_KEYCLOAK_CLIENT_ID,
    redirectUri: import.meta.env.NG_APP_KEYCLOAK_REDIRECT_URI,
  },
  api: {
    gateway_url: import.meta.env.NG_APP_API_GATEWAY_URL,
  }
};
