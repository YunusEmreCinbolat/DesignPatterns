import { Component } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { CartService } from '../../services/cart-service';

@Component({
  selector: 'app-header',
  standalone: true,                      // ✅ standalone
  imports: [CommonModule, DecimalPipe],  // ✅ number pipe için CommonModule/DecimalPipe
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  count = 0;
  total = 0;

  constructor(private cartService: CartService) {
    this.cartService.state$.subscribe(s => {
      this.count = s.cart.items?.reduce((a, i) => a + i.quantity, 0) ?? 0;
      this.total = s.totals.grandTotal;
    });
  }
}
