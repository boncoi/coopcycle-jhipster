import { IUser } from 'app/entities/user/user.model';

export interface IAdministrator {
  id?: number;
  levelOfAuthority?: number;
  imageProfilContentType?: string | null;
  imageProfil?: string | null;
  mobilePhone?: string;
  user?: IUser | null;
}

export class Administrator implements IAdministrator {
  constructor(
    public id?: number,
    public levelOfAuthority?: number,
    public imageProfilContentType?: string | null,
    public imageProfil?: string | null,
    public mobilePhone?: string,
    public user?: IUser | null
  ) {}
}

export function getAdministratorIdentifier(administrator: IAdministrator): number | undefined {
  return administrator.id;
}
