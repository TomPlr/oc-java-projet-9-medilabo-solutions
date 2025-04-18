# Medilabo Solutions UI

This is the frontend application for Medilabo Solutions.

## Environment Configuration

This project uses environment variables for configuration. The configuration is managed through a `.env` file in the root of the project.

### Setting Up Environment Variables

1. Copy the `.env.example` file to `.env`:
   ```
   cp .env.example .env
   ```

2. Edit the `.env` file with your configuration values:
   ```
   # Keycloak Configuration
   KEYCLOAK_URL=http://localhost:8080
   KEYCLOAK_REALM=medilabo
   KEYCLOAK_CLIENT_ID=medilabo-ui
   KEYCLOAK_REDIRECT_URI=http://localhost:4200

   # API Configuration
   API_URL=http://localhost:8081
   API_VERSION=v1
   ```

### How It Works

The environment variables are loaded at build time using a script that replaces the values in the environment files. This approach allows you to:

1. Keep sensitive information out of your codebase
2. Have different configurations for different environments
3. Easily change configuration values without modifying the code

### Development

To start the development server with your environment variables:

```
npm start
```

This will run the environment variable replacement script and then start the Angular development server.

### Production Build

To build the application for production with your environment variables:

```
npm run build
```

This will run the environment variable replacement script and then build the application for production.

## Keycloak Integration

This application uses Keycloak for authentication and authorization. Make sure your Keycloak server is running and configured correctly before starting the application.

### Keycloak Configuration

1. Create a realm in Keycloak
2. Create a client in the realm
3. Configure the client with:
   - Access Type: `public`
   - Standard Flow Enabled: `ON`
   - Direct Access Grants Enabled: `ON`
   - Valid Redirect URIs: Add your application URLs
   - Web Origins: Add your application URLs

4. Update the `.env` file with your Keycloak configuration

## API Integration

The application communicates with a backend API. Make sure your API server is running and configured correctly before starting the application.

### API Configuration

Update the `.env` file with your API configuration:

```
API_URL=http://localhost:8081
API_VERSION=v1
```

# MsUi

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 19.2.0.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Karma](https://karma-runner.github.io) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
