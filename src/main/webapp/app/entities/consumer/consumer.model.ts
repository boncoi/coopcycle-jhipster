import { IUser } from 'app/entities/user/user.model';

export interface IConsumer {
  id?: number;
  address?: string;
  imageProfilContentType?: string | null;
  imageProfil?: string | null;
  mobilePhone?: string;
  user?: IUser | null;
}

export class Consumer implements IConsumer {
  constructor(
    public id?: number,
    public address?: string,
    public imageProfilContentType?: string | null,
    public imageProfil?: string | null,
    public mobilePhone?: string,
    public user?: IUser | null
  ) {}
}

export function getConsumerIdentifier(consumer: IConsumer): number | undefined {
  return consumer.id;
}
