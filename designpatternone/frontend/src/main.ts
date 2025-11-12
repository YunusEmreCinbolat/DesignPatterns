import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';           // ⬅️ BUNU EKLE
import { provideRouter, Routes } from '@angular/router';
import { App } from './app/app';
import { CartComponent } from './app/components/cart/cart.component';

const routes: Routes = [
  { path: '', component: CartComponent }
];

bootstrapApplication(App, {
  providers: [
    provideHttpClient(),                                             // ⬅️ BUNU EKLE
    provideRouter(routes)
  ]
}).catch(err => console.error(err));
