import {ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot, UrlTree} from '@angular/router';
import {AuthGuardData, createAuthGuard} from 'keycloak-angular';
import {Role} from '../models/role.model';
import {environment} from '../../environments/environment';

const isAccessAllowed = async (
  route: ActivatedRouteSnapshot,
  _: RouterStateSnapshot,
  authData: AuthGuardData
): Promise<boolean | UrlTree> => {
  const {authenticated, grantedRoles} = authData;
  const roles = [Role.MANAGER, Role.MEDICAL_STAFF];
  const hasRequiredRole = roles.some(role => grantedRoles.resourceRoles[environment.keycloak.clientId].includes(role))

  return authenticated && hasRequiredRole;
}


export const canActivateAuthRole = createAuthGuard<CanActivateFn>(isAccessAllowed);
