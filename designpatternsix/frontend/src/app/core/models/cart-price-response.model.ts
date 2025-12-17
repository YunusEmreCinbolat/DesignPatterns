export interface CartPriceResponse {
  subtotal: number;
  discount: number;
  totalAfterDiscount: number;
  shippingFee: number;
  finalTotal: number;
  description: string;
}
