package model;

import java.time.LocalDateTime;
import java.util.List;

public class Invoice {
    private int id;
    private int customerId;
    private int userId;
    private double totalAmount;
    private double paidAmount;
    private double changeAmount;
    private String note;
    private LocalDateTime createdAt;

    private List<InvoiceItem> items; // Danh sách chi tiết hóa đơn (tùy dùng)

    // Constructors
    public Invoice() {}

    public Invoice(int customerId, int userId, double totalAmount, double paidAmount, double changeAmount, String note) {
        this.customerId = customerId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.changeAmount = changeAmount;
        this.note = note;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }
}
