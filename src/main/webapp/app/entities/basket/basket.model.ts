import { IUser } from 'app/entities/user/user.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IBasket {
  id?: number;
  idBasket?: string;
  user?: IUser | null;
  idProducts?: IProduct[] | null;
}

export class Basket implements IBasket {
  constructor(public id?: number, public idBasket?: string, public user?: IUser | null, public idProducts?: IProduct[] | null) {}
}

export function getBasketIdentifier(basket: IBasket): number | undefined {
  return basket.id;
}
