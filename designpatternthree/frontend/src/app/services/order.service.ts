import { Injectable } from '@angular/core';
import { ApiProxyService } from './api-proxy.service';

import { ProductBundle } from '../models/product-bundle.model';
import { SingleProduct } from '../models/single-product.model';

import { FrontendOrder } from '../models/frontend-order.model';
import { EmptyCartCheckHandler } from './chain/empty-cart-check.handler';
import { TermsAcceptanceHandler } from './chain/terms-acceptance.handler';
import { MinAmountHandler } from './chain/min-amount.handler';
import { SimpleFraudCheckHandler } from './chain/simple-fraud-check.handler';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  constructor(private api: ApiProxyService) {}

  /**
   * FRONTEND → BACKEND Sipariş oluşturma
   * Composite + Chain + Proxy Pattern
   */
  createOrder(
    cart: { id: string; name: string; price: number; qty: number }[],
    userAcceptedTerms: boolean
  ) {
    const bundle = new ProductBundle('Order Bundle');
    const items: { productId: string; quantity: number }[] = [];

    cart.forEach((p) => {
      const product = new SingleProduct(p.id, p.name, p.price);

      for (let i = 0; i < p.qty; i++) {
        bundle.add(product);
      }

      items.push({
        productId: p.id,
        quantity: p.qty,
      });
    });

    const totalPrice = bundle.getPrice();

    const order: FrontendOrder = {
      items,
      totalPrice,
      paymentCompleted: true,
      notFraud: true,
      addressValid: true,
      userAcceptedTerms,
    };

    const chain = new EmptyCartCheckHandler();

    chain
      .setNext(new TermsAcceptanceHandler())
      .setNext(new MinAmountHandler(100))
      .setNext(new SimpleFraudCheckHandler());

    chain.handle(order);

    return this.api.request('POST', '/orders', order);
  }

  // ----------------------------------------------------------
  //  🚀 Yeni Eklenen Servis Metotları (BURADAN SONRASI EK)
  // ----------------------------------------------------------

  /** Sipariş detayını getir (Order-Status sayfasında kullanılır) */
  getOrder(id: number) {
    return this.api.request('GET', `/orders/${id}`);
  }

  /** Bir sonraki statüye geçir (Chain of Responsibility backend'de çalışır) */
  nextStatus(id: number) {
    return this.api.request('POST', `/orders/${id}/next`, {});
  }
}
