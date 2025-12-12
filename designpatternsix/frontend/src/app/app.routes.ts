import { Routes } from '@angular/router';
import { PizzaOrderComponent } from './features/pizza-order/pizza-order.component';
import { OrderListComponent } from './features/order-list/order-list.component';

export const routes: Routes = [
  { path: '', component: PizzaOrderComponent },
  { path: 'orders', component: OrderListComponent },
];
