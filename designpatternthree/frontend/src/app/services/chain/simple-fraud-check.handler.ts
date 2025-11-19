import { OrderFrontHandler } from './order-front-handler';
import { FrontendOrder } from '../../models/frontend-order.model';

export class SimpleFraudCheckHandler extends OrderFrontHandler {
  protected doHandle(order: FrontendOrder): boolean {
    if (order.totalPrice > 100000) {
      console.warn('[Fraud Warning] Çok yüksek tutarlı sipariş tespit edildi.');
    }
    return true;
  }
}
