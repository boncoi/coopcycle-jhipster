import { IUser } from 'app/entities/user/user.model';
import { IProduct } from 'app/entities/product/product.model';
import { ICooperative } from 'app/entities/cooperative/cooperative.model';
import { MerchantType } from 'app/entities/enumerations/merchant-type.model';

export interface IMerchant {
  id?: number;
  merchantName?: string;
  address?: string;
  merchantType?: MerchantType;
  imageProfilContentType?: string | null;
  imageProfil?: string | null;
  mobilePhone?: string;
  user?: IUser | null;
  products?: IProduct[] | null;
  cooperative?: ICooperative | null;
}

export class Merchant implements IMerchant {
  constructor(
    public id?: number,
    public merchantName?: string,
    public address?: string,
    public merchantType?: MerchantType,
    public imageProfilContentType?: string | null,
    public imageProfil?: string | null,
    public mobilePhone?: string,
    public user?: IUser | null,
    public products?: IProduct[] | null,
    public cooperative?: ICooperative | null
  ) {}
}

export function getMerchantIdentifier(merchant: IMerchant): number | undefined {
  return merchant.id;
}
