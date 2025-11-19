import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  loading = false;
  cart: any[] = [];

  constructor(
    private orderService: OrderService,
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit() {
    this.cartService.cart$.subscribe((cart) => {
      this.cart = cart;
    });
  }

  get itemCount() {
    return this.cart.reduce((t, i) => t + i.qty, 0);
  }

  get totalPrice() {
    return this.cart.reduce((t, i) => t + i.qty * i.price, 0);
  }

  submit() {
    this.loading = true;

    this.orderService.createOrder(this.cart, true).subscribe({
      next: (order: any) => {
        this.loading = false;

        this.cartService.clear(); // ✔ sipariş sonrası sepeti boşalt
        this.router.navigate(['/order-status', order.id]);
      },
      error: (err) => {
        this.loading = false;
        alert(err.message || "Sipariş oluşturulamadı!");
      }
    });
  }
}
