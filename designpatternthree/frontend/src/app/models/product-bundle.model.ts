import { ProductComponent } from './product-component.interface';

export class ProductBundle implements ProductComponent {
  private items: ProductComponent[] = [];

  constructor(private bundleName: string) {}

  add(component: ProductComponent): void {
    this.items.push(component);
  }

  remove(component: ProductComponent): void {
    this.items = this.items.filter(item => item !== component);
  }

  getItems(): ProductComponent[] {
    return this.items;
  }

  getName(): string {
    return this.bundleName;
  }

  getPrice(): number {
    return this.items.reduce((sum, item) => sum + item.getPrice(), 0);
  }

  print(indent: number = 0): void {
    console.log(`${' '.repeat(indent)}Bundle: ${this.bundleName}`);
    this.items.forEach(item => item.print(indent + 2));
  }
}
