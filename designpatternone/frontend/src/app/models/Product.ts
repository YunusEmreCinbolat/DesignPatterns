// models/product.ts
export interface Product {
  id: string; name: string; price: number; type: 'BOOK'|'ELECTRONIC'|'CLOTHING';
  author?: string; warrantyMonths?: number; size?: string;
}
