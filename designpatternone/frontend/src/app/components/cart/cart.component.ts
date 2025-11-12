// src/app/components/cart/cart.component.ts
import { Component } from '@angular/core';
import { CommonModule, DecimalPipe, NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import { Cart } from '../../models/Cart';
import { Totals } from '../../models/Totals';
import { DiscountCode } from '../../models/Discount';
import { CartService } from '../../services/cart-service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule, NgIf, NgFor, DecimalPipe],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent {
  // tip tanımı: { cart: Cart; totals: Totals }
  vm$!: Observable<{ cart: Cart; totals: Totals }>;
  code: DiscountCode = 'PERC10';

  constructor(private cs: CartService) {
    this.vm$ = this.cs.state$;         // ✅ constructor içinde ata
  }

  remove(id: string) { this.cs.remove(id).subscribe(); }
  clear() { this.cs.clear().subscribe(); }
  apply() { this.cs.applyDiscount(this.code).subscribe(); }
}
