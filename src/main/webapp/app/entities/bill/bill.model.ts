import * as dayjs from 'dayjs';
import { IBasket } from 'app/entities/basket/basket.model';
import { TypePayment } from 'app/entities/enumerations/type-payment.model';
import { CommandStatus } from 'app/entities/enumerations/command-status.model';

export interface IBill {
  id?: number;
  date?: dayjs.Dayjs;
  payment?: TypePayment;
  status?: CommandStatus;
  totalPrice?: number;
  idBasket?: IBasket | null;
}

export class Bill implements IBill {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public payment?: TypePayment,
    public status?: CommandStatus,
    public totalPrice?: number,
    public idBasket?: IBasket | null
  ) {}
}

export function getBillIdentifier(bill: IBill): number | undefined {
  return bill.id;
}
