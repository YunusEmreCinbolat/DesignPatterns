import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-order-status',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order-status.component.html',
  styleUrls: ['./order-status.component.css']
})
export class OrderStatusComponent implements OnInit, OnDestroy {

  order: any;
  intervalId: any;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.params['id']);

    // İlk sipariş yükleme
    this.loadOrder(id);

    // Her 30 saniyede bir statü ilerlet
    this.intervalId = setInterval(() => {
      this.orderService.nextStatus(id).subscribe({
        next: res => this.order = res,
        error: err => console.error(err)
      });
    }, 7000);
  }

  loadOrder(id: number) {
    this.orderService.getOrder(id).subscribe({
      next: res => this.order = res,
      error: err => console.error(err)
    });
  }

  ngOnDestroy() {
    clearInterval(this.intervalId);
  }
}
