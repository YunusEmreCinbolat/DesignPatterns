export interface FrontendOrder {
  items: {
    productId: string;
    quantity: number;
  }[];

  totalPrice: number;

  paymentCompleted: boolean;
  notFraud: boolean;
  addressValid: boolean;

  userAcceptedTerms: boolean;
}
