import { Pipe, PipeTransform } from '@angular/core';
import { Address } from '../models/address.model';

@Pipe({
  name: 'address',
  standalone: true
})
export class AddressPipe implements PipeTransform {
  transform(address: Address | undefined): string {
    if (!address) return 'N/A';
    return `${address.street}, ${address.postalCode} ${address.city}`;
  }
} 