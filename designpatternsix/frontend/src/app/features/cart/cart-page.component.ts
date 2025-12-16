import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Product } from '../../core/models/product.model';
import { CartItem } from '../../core/models/cart-item.model';
import { DiscountType, CartPriceRequest } from '../../core/models/cart-price-request.model';
import { CartPriceResponse } from '../../core/models/cart-price-response.model';
import { DiscountApiService } from '../../core/services/discount-api.service';
import { ProductApiService } from '../../core/services/product-api.service';

@Component({
  selector: 'app-cart-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cart-page.component.html',
  styleUrls: ['./cart-page.component.css'],
})
export class CartPageComponent implements OnInit {
  // Flyweight ürün listesi (backend’ten geliyor)
  products: Product[] = [];

  // Sepetteki satırlar
  cartItems: CartItem[] = [];

  // Kullanıcının seçtiği ürün + adet
  selectedProductId: string = '';
  selectedQty: number = 1;
  selectedProduct: Product | null = null;

  // Seçili indirim tipi
  discountType: DiscountType = 'NONE';
  discountTypes: DiscountType[] = [];

  // Sonuç
  result: CartPriceResponse | null = null;
  loading = false;
  errorMessage = '';

  constructor(
    private productApi: ProductApiService,
    private discountApi: DiscountApiService
  ) {}

  ngOnInit(): void {
    // Uygulama açılır açılmaz 10 ürünü çek
    this.productApi.getProducts().subscribe({
      next: (products) => {
        this.products = products;
        console.log('[FE] Loaded products from backend →', products);
      },
      error: (err) => {
        console.error('[FE] Failed to load products', err);
        const status = err?.status;
        this.errorMessage =
          status === 0
            ? 'Backend erişilemiyor (çalışmıyor olabilir).'
            : `Products could not be loaded. (HTTP ${status})`;
      },
    });

    this.discountApi.getDiscountTypes().subscribe({
      next: (types) => {
        this.discountTypes = types;
        if (!this.discountTypes.includes(this.discountType) && this.discountTypes.length > 0) {
          this.discountType = this.discountTypes[0];
        }
      },
      error: (err) => {
        console.error('[FE] Failed to load discount types', err);
        this.discountTypes = ['NONE', 'PERCENTAGE', 'BUY_X_GET_Y', 'FREE_SHIPPING'];
      },
    });
  }

  onProductChange() {
    this.selectedProduct =
      this.products.find((p) => p.id === this.selectedProductId) || null;
  }

  addToCart() {
    if (!this.selectedProduct) {
      alert('Lütfen bir ürün seçin.');
      return;
    }
    if (this.selectedQty <= 0) {
      alert('Miktar 1 veya daha büyük olmalı.');
      return;
    }

    // Sepette aynı üründen varsa miktara ekleyelim
    const existing = this.cartItems.find(
      (i) => i.productId === this.selectedProduct!.id
    );

    if (existing) {
      existing.quantity += this.selectedQty;
    } else {
      this.cartItems.push({
        productId: this.selectedProduct.id,
        name: this.selectedProduct.name,
        price: this.selectedProduct.price,
        quantity: this.selectedQty,
      });
    }

    // Seçimleri resetle
    this.selectedProductId = '';
    this.selectedProduct = null;
    this.selectedQty = 1;
  }

  removeItem(index: number) {
    this.cartItems.splice(index, 1);
  }

  calculate() {
    if (this.cartItems.length === 0) {
      alert('Sepete en az bir ürün ekleyin.');
      return;
    }

    const payload: CartPriceRequest = {
      items: this.cartItems,
      discountType: this.discountType,
    };

    this.loading = true;
    this.errorMessage = '';
    this.result = null;

    this.discountApi.calculatePrice(payload).subscribe({
      next: (res) => {
        this.result = res;
        this.loading = false;
      },
      error: (err) => {
        console.error('[FE] Error while calculating discount', err);
        this.errorMessage = 'Hesaplama sırasında bir hata oluştu.';
        this.loading = false;
      },
    });
  }

  get cartSubtotal(): number {
    return this.cartItems.reduce(
      (sum, item) => sum + item.price * item.quantity,
      0
    );
  }

  getDiscountLabel(type: DiscountType): string {
    switch (type) {
      case 'NONE':
        return 'No Discount';
      case 'PERCENTAGE':
        return '10% Discount';
      case 'BUY_X_GET_Y':
        return 'Buy 2 Get 1';
      case 'FREE_SHIPPING':
        return 'Free Shipping';
      default:
        return type;
    }
  }
}
