import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DiscountApiService } from '../../core/services/discount-api.service';
import { CartItem } from '../../core/models/cart-item.model';
import { CartPriceRequest, DiscountType } from '../../core/models/cart-price-request.model';
import { CartPriceResponse } from '../../core/models/cart-price-response.model';

@Component({
  selector: 'app-cart-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cart-page.component.html',
  styleUrls: ['./cart-page.component.css'],
})
export class CartPageComponent {
  items: CartItem[] = [
    { productId: 'P1', name: 'Mouse', price: 200, quantity: 1 },
    { productId: 'P2', name: 'Keyboard', price: 400, quantity: 1 },
  ];

  newItem: CartItem = { productId: '', name: '', price: 0, quantity: 1 };

  discountType: DiscountType = 'NONE';

  result: CartPriceResponse | null = null;
  loading = false;
  errorMessage = '';

  constructor(private api: DiscountApiService) {}

  addItem() {
    if (!this.newItem.productId || !this.newItem.name || this.newItem.price <= 0 || this.newItem.quantity <= 0) {
      alert('Lütfen geçerli ürün bilgileri giriniz.');
      return;
    }
    this.items.push({ ...this.newItem });
    this.newItem = { productId: '', name: '', price: 0, quantity: 1 };
  }

  removeItem(index: number) {
    this.items.splice(index, 1);
  }

  calculate() {
    if (this.items.length === 0) {
      alert('Sepete en az bir ürün ekleyin.');
      return;
    }

    const payload: CartPriceRequest = {
      items: this.items,
      discountType: this.discountType,
    };

    this.loading = true;
    this.errorMessage = '';
    this.result = null;

    this.api.calculatePrice(payload).subscribe({
      next: (res) => {
        this.result = res;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Hesaplama sırasında bir hata oluştu.';
        this.loading = false;
      },
    });
  }
}
