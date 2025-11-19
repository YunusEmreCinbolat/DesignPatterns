import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CartService {

  private cartItems = new BehaviorSubject<any[]>([]);
  cart$ = this.cartItems.asObservable();

  constructor() {
    // localStorage'dan yükle
    const saved = localStorage.getItem('cart');
    if (saved) {
      this.cartItems.next(JSON.parse(saved));
    }
  }

  // ✔ LocalStorage ile state senkronize et
  private save(cart: any[]) {
    localStorage.setItem('cart', JSON.stringify(cart));
    this.cartItems.next(cart);
  }

  // ✔ Şu anki sepet
  getCart() {
    return this.cartItems.getValue();
  }

  // ✔ Ürün ekle
  addToCart(product: any) {
    const cart = this.getCart();
    const item = cart.find(x => x.id === product.id);

    if (item) {
      item.qty++;
    } else {
      cart.push({ ...product, qty: 1 });
    }

    this.save(cart);
  }

  // ✔ Ürün sil
  remove(id: string) {
    const cart = this.getCart().filter(x => x.id !== id);
    this.save(cart);
  }

  // ✔ Adet artır
  increase(id: string) {
    const cart = this.getCart();
    const item = cart.find(x => x.id === id);
    if (item) {
      item.qty++;
      this.save(cart);
    }
  }

  // ✔ Adet azalt
  decrease(id: string) {
    const cart = this.getCart();
    const item = cart.find(x => x.id === id);
    if (item && item.qty > 1) {
      item.qty--;
      this.save(cart);
    }
  }

  // ✔ Sepeti boşalt
  clear() {
    this.save([]);
  }

  // ✔ Toplam ürün adedi
  getTotalItems() {
    return this.getCart().reduce((t, item) => t + item.qty, 0);
  }

  // ✔ Toplam fiyat
  getTotalPrice() {
    return this.getCart().reduce((t, item) => t + item.qty * item.price, 0);
  }
}
