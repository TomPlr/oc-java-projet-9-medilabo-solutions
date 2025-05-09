// Define the type of the environment variables.
declare interface Env {
  readonly NODE_ENV: string;

  // Keycloak Configuration
  readonly NG_APP_KEYCLOAK_URL: string;
  readonly NG_APP_KEYCLOAK_REALM: string;
  readonly NG_APP_KEYCLOAK_CLIENT_ID: string;
  readonly NG_APP_KEYCLOAK_REDIRECT_URI: string;


  // API Configuration
  readonly NG_APP_API_GATEWAY_URL: string;

}

declare interface ImportMeta {
  readonly env: Env;
}
