import { FrontendOrder } from '../../models/frontend-order.model';

export abstract class OrderFrontHandler {
  protected next: OrderFrontHandler | null = null;

  setNext(handler: OrderFrontHandler) {
    this.next = handler;
    return handler;
  }

  handle(order: FrontendOrder) {
    const ok = this.doHandle(order);
    if (ok && this.next) {
      this.next.handle(order);
    }
  }

  protected abstract doHandle(order: FrontendOrder): boolean;
}
