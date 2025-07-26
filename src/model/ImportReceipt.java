package model;

import java.util.Date;
import java.util.List;

public class ImportReceipt {
    private int id;
    private Date importDate;
    private int userId;
    private String note;
    private double totalAmount;
    private String supplierName;
    private String supplierPhone;

    private List<ImportItem> items; // Danh sách sản phẩm nhập trong phiếu

    public ImportReceipt() {}

    public ImportReceipt(int id, Date importDate, int userId, String note, double totalAmount, String supplierName, String supplierPhone) {
        this.id = id;
        this.importDate = importDate;
        this.userId = userId;
        this.note = note;
        this.totalAmount = totalAmount;
        this.supplierName = supplierName;
        this.supplierPhone = supplierPhone;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getImportDate() { return importDate; }
    public void setImportDate(Date importDate) { this.importDate = importDate; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getSupplierPhone() { return supplierPhone; }
    public void setSupplierPhone(String supplierPhone) { this.supplierPhone = supplierPhone; }

    public List<ImportItem> getItems() { return items; }
    public void setItems(List<ImportItem> items) { this.items = items; }
}
