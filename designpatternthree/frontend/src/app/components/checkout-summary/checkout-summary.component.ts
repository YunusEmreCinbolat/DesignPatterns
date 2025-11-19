import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-checkout-summary',
  standalone: true,
  imports: [CommonModule,CurrencyPipe],
  templateUrl: './checkout-summary.component.html',
  styleUrls: ['./checkout-summary.component.css'],
})
export class CheckoutSummaryComponent {
  @Input() totalPrice: number = 0;
  @Input() itemCount: number = 0;
  @Input() loading: boolean = false;

  @Output() submitOrder = new EventEmitter<void>();

  onCheckout() {
    this.submitOrder.emit();
  }
}
