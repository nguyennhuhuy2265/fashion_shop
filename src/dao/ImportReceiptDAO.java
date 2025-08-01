package dao;

import model.ImportReceipt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImportReceiptDAO {

    public int insertReceipt(ImportReceipt receipt) {
        String sql = "INSERT INTO import_receipts (supplier_id, user_id, total_amount, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, receipt.getSupplierId());
            stmt.setInt(2, receipt.getUserId());
            stmt.setDouble(3, receipt.getTotalAmount());
            stmt.setTimestamp(4, Timestamp.valueOf(receipt.getCreatedAt()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // return new ID
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<ImportReceipt> getAllReceipts() {
        List<ImportReceipt> list = new ArrayList<>();
        String sql = "SELECT * FROM import_receipts ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ImportReceipt r = new ImportReceipt();
                r.setId(rs.getInt("id"));
                r.setSupplierId(rs.getInt("supplier_id"));
                r.setUserId(rs.getInt("user_id"));
                r.setTotalAmount(rs.getDouble("total_amount"));
                r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ImportReceipt getReceiptById(int id) {
        String sql = "SELECT * FROM import_receipts WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ImportReceipt r = new ImportReceipt();
                    r.setId(rs.getInt("id"));
                    r.setSupplierId(rs.getInt("supplier_id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setTotalAmount(rs.getDouble("total_amount"));
                    r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return r;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSupplierNameById(int id) {
        String sql = "SELECT name FROM suppliers WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
