export interface ProductComponent {
  getName(): string;
  getPrice(): number;
  print(indent?: number): void;
}
