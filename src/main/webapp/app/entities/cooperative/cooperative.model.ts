import { IMerchant } from 'app/entities/merchant/merchant.model';
import { ICourier } from 'app/entities/courier/courier.model';

export interface ICooperative {
  id?: number;
  cooperativeName?: string;
  merchants?: IMerchant[] | null;
  couriers?: ICourier[] | null;
}

export class Cooperative implements ICooperative {
  constructor(
    public id?: number,
    public cooperativeName?: string,
    public merchants?: IMerchant[] | null,
    public couriers?: ICourier[] | null
  ) {}
}

export function getCooperativeIdentifier(cooperative: ICooperative): number | undefined {
  return cooperative.id;
}
