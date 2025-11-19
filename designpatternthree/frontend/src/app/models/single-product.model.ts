import { ProductComponent } from './product-component.interface';

export class SingleProduct implements ProductComponent {
  constructor(
    private id: string,
    private name: string,
    private price: number
  ) {}

  getId() {
    return this.id;
  }

  getName(): string {
    return this.name;
  }

  getPrice(): number {
    return this.price;
  }

  print(indent: number = 0): void {
    console.log(`${' '.repeat(indent)}- ${this.name}: ${this.price}₺`);
  }
}
 