import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-cart-item',
  standalone: true,
  imports: [CommonModule,CurrencyPipe,CartItemComponent],
  templateUrl: './cart-item.component.html',
  styleUrls: ['./cart-item.component.css'],
})
export class CartItemComponent {
  @Input() item!: {
    id: string;
    name: string;
    price: number;
    qty: number;
  };

  @Output() remove = new EventEmitter<string>();
  @Output() increase = new EventEmitter<string>();
  @Output() decrease = new EventEmitter<string>();

  onRemove() {
    this.remove.emit(this.item.id);
  }

  onIncrease() {
    this.increase.emit(this.item.id);
  }

  onDecrease() {
    if (this.item.qty > 1) this.decrease.emit(this.item.id);
  }
}
