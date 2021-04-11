import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'consumer',
        data: { pageTitle: 'coopcycleApp.consumer.home.title' },
        loadChildren: () => import('./consumer/consumer.module').then(m => m.ConsumerModule),
      },
      {
        path: 'courier',
        data: { pageTitle: 'coopcycleApp.courier.home.title' },
        loadChildren: () => import('./courier/courier.module').then(m => m.CourierModule),
      },
      {
        path: 'merchant',
        data: { pageTitle: 'coopcycleApp.merchant.home.title' },
        loadChildren: () => import('./merchant/merchant.module').then(m => m.MerchantModule),
      },
      {
        path: 'cooperative',
        data: { pageTitle: 'coopcycleApp.cooperative.home.title' },
        loadChildren: () => import('./cooperative/cooperative.module').then(m => m.CooperativeModule),
      },
      {
        path: 'administrator',
        data: { pageTitle: 'coopcycleApp.administrator.home.title' },
        loadChildren: () => import('./administrator/administrator.module').then(m => m.AdministratorModule),
      },
      {
        path: 'basket',
        data: { pageTitle: 'coopcycleApp.basket.home.title' },
        loadChildren: () => import('./basket/basket.module').then(m => m.BasketModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'coopcycleApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'bill',
        data: { pageTitle: 'coopcycleApp.bill.home.title' },
        loadChildren: () => import('./bill/bill.module').then(m => m.BillModule),
      },
      {
        path: 'notification',
        data: { pageTitle: 'coopcycleApp.notification.home.title' },
        loadChildren: () => import('./notification/notification.module').then(m => m.NotificationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
