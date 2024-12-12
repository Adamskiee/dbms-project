package models;

public class SoldItem {
    private int productId;
    private String productName;
    private int quantity;
    private double subtotal;

    public SoldItem(int productId, String productName, int quantity, double subtotal) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTotalPrice() {
        return quantity * subtotal;
    }
}
