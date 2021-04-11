import { IUser } from 'app/entities/user/user.model';
import { ICooperative } from 'app/entities/cooperative/cooperative.model';

export interface ICourier {
  id?: number;
  working?: boolean;
  imageProfilContentType?: string | null;
  imageProfil?: string | null;
  mobilePhone?: string;
  user?: IUser | null;
  cooperative?: ICooperative | null;
}

export class Courier implements ICourier {
  constructor(
    public id?: number,
    public working?: boolean,
    public imageProfilContentType?: string | null,
    public imageProfil?: string | null,
    public mobilePhone?: string,
    public user?: IUser | null,
    public cooperative?: ICooperative | null
  ) {
    this.working = this.working ?? false;
  }
}

export function getCourierIdentifier(courier: ICourier): number | undefined {
  return courier.id;
}
