package models;

public class SoldItemTransaction {
    private int saleId;
    private String productName;
    private int quantity;
    private String date;
    private double subtotal;

    public SoldItemTransaction(int saleId, String date, String productName, int quantity, double subtotal){
        this.date = date;
        this.saleId = saleId;
        this.productName = productName;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public String getProductName() {
        return productName;
    }
    public String getDate() {
        return date;
    }
    public int getQuantity() {
        return quantity;
    }
    public int getSaleId() {
        return saleId;
    }
    public double getSubtotal() {
        return subtotal;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
