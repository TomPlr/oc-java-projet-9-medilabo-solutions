export const environment = {
  production: false,
  keycloak: {
    url: import.meta.env.NG_APP_KEYCLOAK_URL || 'http://localhost:8081',
    realm: import.meta.env.NG_APP_KEYCLOAK_REALM || 'medilabo',
    clientId: import.meta.env.NG_APP_KEYCLOAK_CLIENT_ID || 'medilabo-auth',
    clientSecret: import.meta.env.NG_APP_KEYCLOAK_CLIENT_SECRET || 'smrh4AtlCHVBW38ZQox492Mgdb8O2XNS',
    redirectUri: import.meta.env.NG_APP_KEYCLOAK_REDIRECT_URI || 'http://localhost:8002',
  },
  api: {
    url: import.meta.env.NG_APP_API_URL || 'http://localhost:8081',
    version: import.meta.env.NG_APP_API_VERSION || 'v1',
  }
}; 