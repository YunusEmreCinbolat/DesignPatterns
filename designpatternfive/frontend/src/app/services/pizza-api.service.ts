import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PizzaOrderRequest } from '../model/PizzaOrderRequest';
import { Order } from '../model/Order';
import { environment } from '../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class PizzaApiService {
  private baseUrl = `${environment.apiUrl}/api/pizzas`;

  constructor(private http: HttpClient) {}

  createOrder(payload: PizzaOrderRequest): Observable<Order> {
    return this.http.post<Order>(`${this.baseUrl}/order`, payload);
  }

  getOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.baseUrl}/orders`);
  }
}
