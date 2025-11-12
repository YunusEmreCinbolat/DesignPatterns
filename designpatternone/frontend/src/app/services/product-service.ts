// services/product.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Product } from '../models/product';
import { environment } from '../environment/environment';
@Injectable({ providedIn: 'root' })
export class ProductService {
 
  private apiUrl = `${environment.apiUrl}/products`;

  constructor(private http: HttpClient) {}
  list() { return this.http.get<Product[]>(this.apiUrl); }
}
