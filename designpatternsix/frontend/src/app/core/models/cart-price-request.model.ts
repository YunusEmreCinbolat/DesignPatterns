import { CartItem } from "./cart-item.model";

export type DiscountType = 'NONE' | 'PERCENTAGE' | 'BUY_X_GET_Y' | 'FREE_SHIPPING';

export interface CartPriceRequest {
  items: CartItem[];
  discountType: DiscountType;
}
