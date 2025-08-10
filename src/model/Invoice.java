package model;

import java.time.LocalDateTime;
import java.util.List;

public class Invoice {
    private int id;
    private int customerId;
    private int userId;
    private int totalAmount;   // đổi từ double sang int
    private int paidAmount;    // đổi từ double sang int
    private int changeAmount;  // đổi từ double sang int
    private String note;
    private LocalDateTime createdAt;

    private List<InvoiceItem> items;

    // Constructors
    public Invoice() {}

    public Invoice(int customerId, int userId, int totalAmount, int paidAmount, int changeAmount, String note) {
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

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(int changeAmount) {
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
