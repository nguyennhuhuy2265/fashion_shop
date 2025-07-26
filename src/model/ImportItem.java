package model;

public class ImportItem {
    private int id;
    private int importId;
    private int productId;
    private int quantity;
    private double importPrice;
    private double subtotal;

    public ImportItem() {}

    public ImportItem(int id, int importId, int productId, int quantity, double importPrice, double subtotal) {
        this.id = id;
        this.importId = importId;
        this.productId = productId;
        this.quantity = quantity;
        this.importPrice = importPrice;
        this.subtotal = subtotal;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getImportId() { return importId; }
    public void setImportId(int importId) { this.importId = importId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getImportPrice() { return importPrice; }
    public void setImportPrice(double importPrice) { this.importPrice = importPrice; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
