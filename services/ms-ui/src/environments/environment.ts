export const environment = {
  production: false,
  keycloak: {
    url: import.meta.env.NG_APP_KEYCLOAK_URL,
    realm: import.meta.env.NG_APP_KEYCLOAK_REALM,
    clientId: import.meta.env.NG_APP_KEYCLOAK_CLIENT_ID,
    clientSecret: import.meta.env.NG_APP_KEYCLOAK_CLIENT_SECRET,
    redirectUri: import.meta.env.NG_APP_KEYCLOAK_REDIRECT_URI,
  },
  api: {
    url: import.meta.env.NG_APP_API_URL,
    version: import.meta.env.NG_APP_API_VERSION,
  }
};
