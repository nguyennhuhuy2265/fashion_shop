package dao;

import model.ImportReceipt;
import java.sql.*;
import java.time.LocalDateTime;
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

    public List<ImportReceipt> getReceiptsBySupplierId(int supplierId) {
        List<ImportReceipt> list = new ArrayList<>();
        String sql = "SELECT * FROM import_receipts WHERE supplier_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, supplierId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ImportReceipt r = new ImportReceipt();
                    r.setId(rs.getInt("id"));
                    r.setSupplierId(rs.getInt("supplier_id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setTotalAmount(rs.getDouble("total_amount"));
                    r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    list.add(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ImportReceipt> searchImportReceiptsById(String keyword) {
        List<ImportReceipt> list = new ArrayList<>();
        String sql = "SELECT * FROM import_receipts WHERE CAST(id AS CHAR) LIKE ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ImportReceipt r = new ImportReceipt();
                    r.setId(rs.getInt("id"));
                    r.setSupplierId(rs.getInt("supplier_id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setTotalAmount(rs.getDouble("total_amount"));
                    r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    list.add(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ImportReceipt> getImportReceiptsByDateRange(LocalDateTime from, LocalDateTime to) {
        List<ImportReceipt> list = new ArrayList<>();
        String sql = "SELECT * FROM import_receipts WHERE created_at BETWEEN ? AND ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(from));
            stmt.setTimestamp(2, Timestamp.valueOf(to));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ImportReceipt r = new ImportReceipt();
                    r.setId(rs.getInt("id"));
                    r.setSupplierId(rs.getInt("supplier_id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setTotalAmount(rs.getDouble("total_amount"));
                    r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    list.add(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ImportReceipt> getImportReceiptsToday() {
        List<ImportReceipt> list = new ArrayList<>();
        String sql = "SELECT * FROM import_receipts WHERE DATE(created_at) = CURDATE() ORDER BY created_at DESC";

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
    // Hàm mới để lấy tổng chi theo năm

    public List<Object[]> getExpensesByYear(int year) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT MONTH(ir.created_at) AS month, SUM(ir.total_amount) AS total_expense "
                + "FROM import_receipts ir "
                + "WHERE YEAR(ir.created_at) = ? "
                + "GROUP BY MONTH(ir.created_at) "
                + "ORDER BY month";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getInt("month"); // Tháng
                row[1] = rs.getDouble("total_expense"); // Tổng chi
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
