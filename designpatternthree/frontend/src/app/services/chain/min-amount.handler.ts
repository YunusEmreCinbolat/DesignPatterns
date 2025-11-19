import { OrderFrontHandler } from './order-front-handler';
import { FrontendOrder } from '../../models/frontend-order.model';

export class MinAmountHandler extends OrderFrontHandler {
  constructor(private minAmount: number) {
    super();
  }

  protected doHandle(order: FrontendOrder): boolean {
    if (order.totalPrice < this.minAmount) {
      throw new Error(`Minimum sipariş tutarı ${this.minAmount}₺ olmalıdır.`);
    }
    return true;
  }
}
