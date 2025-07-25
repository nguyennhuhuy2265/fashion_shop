package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Invoice {
    private int id;
    private int customerId;
    private int userId;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private int pointsUsed;
    private int pointsEarned;

    public Invoice() {}

    public Invoice(int id, int customerId, int userId, LocalDateTime createdAt, BigDecimal totalAmount, int pointsUsed, int pointsEarned) {
        this.id = id;
        this.customerId = customerId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
        this.pointsUsed = pointsUsed;
        this.pointsEarned = pointsEarned;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public int getPointsUsed() { return pointsUsed; }
    public void setPointsUsed(int pointsUsed) { this.pointsUsed = pointsUsed; }

    public int getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }
}
