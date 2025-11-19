import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-order-status',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order-status.component.html',
  styleUrls: ['./order-status.component.css'],
})
export class OrderStatusComponent implements OnInit, OnDestroy {
  order: any;
  intervalId: any;
  orderId!: number;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    this.orderId = Number(this.route.snapshot.params['id']);

    // İlk yükleme
    this.loadStatus();

    // Her 30 saniyede bir ilerlet
    this.intervalId = setInterval(() => {
      this.progressStatus();
    }, 3000);
  }

  loadStatus() {
    this.orderService.getOrder(this.orderId).subscribe((res) => {
      this.order = res;
      this.stopIfCompleted();
    });
  }

  progressStatus() {
    this.orderService.nextStatus(this.orderId).subscribe((res) => {
      this.order = res;
      this.stopIfCompleted();
    });
  }

  stopIfCompleted() {
    if (this.order.status === 'DELIVERED' || this.order.status === 'COMPLETED') {
      console.log("✅ Sipariş tamamlandı, interval durduruldu.");
      clearInterval(this.intervalId);
    }
  }

  ngOnDestroy() {
    clearInterval(this.intervalId);
  }
}
