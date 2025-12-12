import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CartPriceRequest, DiscountType } from '../models/cart-price-request.model';
import { CartPriceResponse } from '../models/cart-price-response.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DiscountApiService {
  private baseUrl = '${environment.apiUrl}/api/cart';

  constructor(private http: HttpClient) {}

  calculatePrice(payload: CartPriceRequest): Observable<CartPriceResponse> {
    return this.http.post<CartPriceResponse>(`${this.baseUrl}/price`, payload);
  }
}
