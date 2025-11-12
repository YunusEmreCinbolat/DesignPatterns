import { Component } from '@angular/core';
import { CommonModule, DecimalPipe, NgFor } from '@angular/common';
import { Product } from '../../models/product';
import { CartService } from '../../services/cart-service';
import { ProductService } from '../../services/product-service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, NgFor, DecimalPipe],   // ✅ number pipe ve *ngFor için
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent {
  products: Product[] = [];

  constructor(private ps: ProductService, private cs: CartService) {
    this.ps.list().subscribe(p => this.products = p);
  }

  add(p: Product) {
    this.cs.add(p.id).subscribe();
  }
}
