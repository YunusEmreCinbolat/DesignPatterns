// services/cart.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, combineLatest, map, tap } from 'rxjs';
import { Cart } from '../models/Cart';
import { Totals } from '../models/Totals';
import { environment } from '../environment/environment';
import { DiscountCode } from '../models/Discount';

@Injectable({ providedIn: 'root' })
export class CartService {
   private apiUrl = `${environment.apiUrl}/cart`;
  
  private cart$   = new BehaviorSubject<Cart>({ items: [], shipping: 0 });
  private totals$ = new BehaviorSubject<Totals>({ itemsTotal: 0, shipping: 0, grandTotal: 0 });

  // ✅ Bileşenlerin abone olacağı tek akış
  readonly state$ = combineLatest([this.cart$, this.totals$]).pipe(
    map(([cart, totals]) => ({ cart, totals }))
  );

  constructor(private http: HttpClient) { this.refresh(); }

  refresh() {
    this.http.get<Cart>(this.apiUrl).subscribe(c => this.cart$.next(c));
    this.http.get<Totals>(`${this.apiUrl}/totals`).subscribe(t => this.totals$.next(t));
  }

  add(productId: string, qty = 1) {
    const params = new HttpParams().set('productId', productId).set('qty', qty);
    return this.http.post(`${this.apiUrl}/add`, null, { params }).pipe(tap(() => this.refresh()));
  }
  remove(productId: string) { return this.http.delete(`${this.apiUrl}/remove/${productId}`).pipe(tap(() => this.refresh())); }
  clear() { return this.http.post(`${this.apiUrl}/clear`, null).pipe(tap(() => this.refresh())); }
  applyDiscount(code: DiscountCode) {
    const params = new HttpParams().set('code', code);
    return this.http.post(`${this.apiUrl}/discount`, null, { params }).pipe(tap(() => this.refresh()));
  }
}
