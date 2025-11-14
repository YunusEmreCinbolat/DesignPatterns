package com.example.backend.product;



import java.util.ArrayList;
import java.util.List;

/**
 * Composite: Set / Bundle ürün.
 * İçinde hem SingleProduct hem başka ProductBundle tutabilir.
 */
public class ProductBundle implements ProductComponent {

    private final String name;
    private final List<ProductComponent> items = new ArrayList<>();

    public ProductBundle(String name) {
        this.name = name;
    }

    public void add(ProductComponent component) {
        items.add(component);
    }

    public void remove(ProductComponent component) {
        items.remove(component);
    }

    public List<ProductComponent> getItems() {
        return items;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Burada toplam fiyatı tüm alt elemanları gezerek hesaplıyoruz.
     * Bu işlem pahalı olabilir: o yüzden üzerine Proxy saracağız.
     */
    @Override
    public double getPrice() {
        return items.stream()
                .mapToDouble(ProductComponent::getPrice)
                .sum();
    }

    @Override
    public void print() {
        System.out.println("Bundle: " + name);
        items.forEach(ProductComponent::print);
    }
}
