export interface PizzaOrderRequest {
  customerName: string;
  size: string;
  doughType: string;
  sauceType: string;
  toppings: string[];
  spicy: boolean;
}

