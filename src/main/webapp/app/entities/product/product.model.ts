import { IMerchant } from 'app/entities/merchant/merchant.model';
import { IBasket } from 'app/entities/basket/basket.model';
import { ProductType } from 'app/entities/enumerations/product-type.model';

export interface IProduct {
  id?: number;
  productID?: string;
  productName?: string;
  productType?: ProductType | null;
  price?: number;
  productImageContentType?: string | null;
  productImage?: string | null;
  merchant?: IMerchant | null;
  idBaskets?: IBasket[] | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public productID?: string,
    public productName?: string,
    public productType?: ProductType | null,
    public price?: number,
    public productImageContentType?: string | null,
    public productImage?: string | null,
    public merchant?: IMerchant | null,
    public idBaskets?: IBasket[] | null
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
