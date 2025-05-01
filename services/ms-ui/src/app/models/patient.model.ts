import {Address} from './address.model';

export enum Gender {
  MALE = 'M',
  FEMALE = 'F'
}

export interface Patient {
  id?: number;
  firstName?: string;
  lastName?: string;
  dateOfBirth?: string;
  gender?: Gender;
  address?: Address;
  phoneNumber?: string;
}
