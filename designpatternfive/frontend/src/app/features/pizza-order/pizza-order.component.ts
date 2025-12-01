import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Order } from '../../model/Order';
import { PizzaOrderRequest } from '../../model/PizzaOrderRequest';
import { PizzaApiService } from '../../services/pizza-api.service';
@Component({
  selector: 'app-pizza-order',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './pizza-order.component.html',
  styleUrls: ['./pizza-order.component.css'],
}) 
export class PizzaOrderComponent {
  customerName = '';
  size = 'MEDIUM';
  doughType = 'REGULAR';
  sauceType = 'TOMATO';
  toppingsText = '';
  spicy = false;

  lastOrder?: Order;
  loading = false;
  errorMessage = '';

  constructor(private api: PizzaApiService) {}

  submit() {
    this.loading = true;
    this.errorMessage = '';

    const toppings = this.toppingsText
      .split(',')
      .map((t) => t.trim())
      .filter((t) => t.length > 0);

    const payload: PizzaOrderRequest = {
      customerName: this.customerName,
      size: this.size,
      doughType: this.doughType,
      sauceType: this.sauceType,
      toppings,
      spicy: this.spicy,
    };

    this.api.createOrder(payload).subscribe({
      next: (order) => {
        this.lastOrder = order;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Order failed';
        this.loading = false;
      },
    });
  }
}
