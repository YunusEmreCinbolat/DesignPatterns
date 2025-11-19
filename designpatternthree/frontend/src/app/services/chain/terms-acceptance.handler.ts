import { OrderFrontHandler } from './order-front-handler';
import { FrontendOrder } from '../../models/frontend-order.model';

export class TermsAcceptanceHandler extends OrderFrontHandler {
  protected doHandle(order: FrontendOrder): boolean {
    if (!order.userAcceptedTerms) {
      throw new Error('Satış sözleşmesini kabul etmelisiniz.');
    }
    return true;
  }
}
