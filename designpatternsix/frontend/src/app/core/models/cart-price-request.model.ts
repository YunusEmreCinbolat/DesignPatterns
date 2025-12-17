import { CartItem } from "./cart-item.model";

export type DiscountType = 'NONE' | 'PERCENTAGE' | 'BUY_X_GET_Y' | 'FREE_SHIPPING';

export const DISCOUNT_TYPE_LABELS: Partial<Record<DiscountType, string>> = {
  NONE: 'No Discount',
  PERCENTAGE: '20% Discount',
  BUY_X_GET_Y: 'Buy 3 Get 1 (P1)',
  FREE_SHIPPING: 'Free Shipping',
};

export interface CartPriceRequest {
  items: CartItem[];
  discountType: DiscountType;
}
