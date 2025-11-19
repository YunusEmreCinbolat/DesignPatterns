import { Injectable } from '@angular/core';
import { ApiProxyService } from './api-proxy.service';
import { Product } from '../models/product.model';
import { ProductBundle } from '../models/product-bundle.model';
import { SingleProduct } from '../models/single-product.model';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  constructor(private api: ApiProxyService) {}

  /** Backend’ten tüm ürünleri getir (cache aktif) */
  getAllProducts() {
    return this.api.request<Product[]>('GET', '/products', undefined, true);
  }

  /** Composite Pattern → ürünleri bundle içerisine çevirir */
  buildComposite(cart: { id: string; name: string; price: number; qty: number }[]) {
    const bundle = new ProductBundle('Cart Bundle');

    cart.forEach((item) => {
      const p = new SingleProduct(item.id, item.name, item.price);

      for (let i = 0; i < item.qty; i++) {
        bundle.add(p);
      }
    });

    return bundle;
  }

  /** Composite üzerinden toplam fiyat hesapla */
  calculateTotal(cart: any[]) {
    return this.buildComposite(cart).getPrice();
  }

  /** Composite debug (istemci tarafında ağaç yapısını görmek için) */
  debugComposite(cart: any[]) {
    const bundle = this.buildComposite(cart);
    console.log('=== PRODUCT COMPOSITE TREE ===');
    bundle.print();
  }
}
