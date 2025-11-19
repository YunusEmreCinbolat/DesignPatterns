import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { CartItemComponent } from '../../components/cart-item/cart-item.component';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule, CartItemComponent],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  cart: any[] = [];

  constructor(
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit() {
    this.cartService.cart$.subscribe(items => {
      this.cart = items;
    });
  }

  increase(id: string) {
    this.cartService.increase(id);
  }

  decrease(id: string) {
    this.cartService.decrease(id);
  }

  remove(id: string) {
    this.cartService.remove(id);
  }

  get totalPrice() {
    return this.cart.reduce((t, i) => t + i.price * i.qty, 0);
  }

  checkout() {
    this.router.navigate(['/checkout']);
  }
}
