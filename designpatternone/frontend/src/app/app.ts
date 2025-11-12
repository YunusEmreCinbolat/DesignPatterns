import { Component } from '@angular/core';
import { HeaderComponent } from './components/header/header.component';
import { CartComponent } from './components/cart/cart.component';
import { ProductListComponent } from './components/product-list/product-list.component';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HeaderComponent, CartComponent, ProductListComponent], // RouterOutlet KALDIRILDI
  template: `
    <app-header></app-header>
    <app-product-list></app-product-list>
    <app-cart></app-cart>
  `
})
export class App {}
