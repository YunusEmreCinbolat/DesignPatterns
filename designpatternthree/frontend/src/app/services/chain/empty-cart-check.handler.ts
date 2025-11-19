import { OrderFrontHandler } from './order-front-handler';
import { FrontendOrder } from '../../models/frontend-order.model';

export class EmptyCartCheckHandler extends OrderFrontHandler {
  protected doHandle(order: FrontendOrder): boolean {
    if (!order.items || order.items.length === 0) {
      throw new Error('Sepetiniz boş.');
    }
    return true;
  }
}
