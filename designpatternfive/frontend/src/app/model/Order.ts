import { Pizza } from "./Pizza";

export interface Order {
  id: number;
  customerName: string;
  pizza: Pizza;
}
