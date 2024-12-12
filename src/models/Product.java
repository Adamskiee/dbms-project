package models;

public class Product {
    private int productID;
    private String productName;
    private double price;
    private int stockQuantity;
    
    public Product(int productID, String productName, double price, int stockQuantity){
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
    public int getProductID() {
        return productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductName() {
        return productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public double getPrice() {
        return price;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    public int getStockQuantity() {
        return stockQuantity;
    }
}
