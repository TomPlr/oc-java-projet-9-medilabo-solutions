import {Address} from './address.model';

export interface Patient {
  id?: number;
  firstName?: string;
  lastName?: string;
  dateOfBirth?: string;
  gender?: string;
  address?: Address;
  phoneNumber?: string;
}
