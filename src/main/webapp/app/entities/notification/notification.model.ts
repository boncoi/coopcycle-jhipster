import * as dayjs from 'dayjs';
import { NotificationType } from 'app/entities/enumerations/notification-type.model';

export interface INotification {
  id?: number;
  date?: dayjs.Dayjs;
  message?: string;
  notifType?: NotificationType | null;
}

export class Notification implements INotification {
  constructor(public id?: number, public date?: dayjs.Dayjs, public message?: string, public notifType?: NotificationType | null) {}
}

export function getNotificationIdentifier(notification: INotification): number | undefined {
  return notification.id;
}
