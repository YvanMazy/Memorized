package be.darkkraft.memorized.example.simple;

public class Product {

    private final String id;
    private double price;
    private int quantity;

    public Product(final String id) {
        this.id = id;
    }

    public Product(final String id, final double price, final int quantity) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return this.id;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" + "id='" + this.id + '\'' + ", price=" + this.price + ", quantity=" + this.quantity + '}';
    }

}