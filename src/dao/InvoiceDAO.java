package dao;

import model.Invoice;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    public int insertInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoices (customer_id, user_id, total_amount, paid_amount, change_amount, note, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, invoice.getCustomerId());
            stmt.setInt(2, invoice.getUserId());
            stmt.setDouble(3, invoice.getTotalAmount());
            stmt.setDouble(4, invoice.getPaidAmount());
            stmt.setDouble(5, invoice.getChangeAmount());
            stmt.setString(6, invoice.getNote());
            stmt.setTimestamp(7, Timestamp.valueOf(invoice.getCreatedAt() != null ? invoice.getCreatedAt() : LocalDateTime.now()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice.setId(generatedKeys.getInt(1));
                    return invoice.getId();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoices ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getInt("id"));
                invoice.setCustomerId(rs.getInt("customer_id"));
                invoice.setUserId(rs.getInt("user_id"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                invoice.setPaidAmount(rs.getDouble("paid_amount"));
                invoice.setChangeAmount(rs.getDouble("change_amount"));
                invoice.setNote(rs.getString("note"));
                invoice.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                list.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Invoice getInvoiceById(int id) {
        String sql = "SELECT * FROM invoices WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getInt("id"));
                invoice.setCustomerId(rs.getInt("customer_id"));
                invoice.setUserId(rs.getInt("user_id"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                invoice.setPaidAmount(rs.getDouble("paid_amount"));
                invoice.setChangeAmount(rs.getDouble("change_amount"));
                invoice.setNote(rs.getString("note"));
                invoice.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return invoice;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Invoice> searchInvoiceById(String keyword) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE id LIKE ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Invoice invoice = extractInvoiceFromResultSet(rs);
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Invoice> getInvoicesByDateRange(LocalDateTime start, LocalDateTime end) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE created_at BETWEEN ? AND ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(start));
            stmt.setTimestamp(2, Timestamp.valueOf(end));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Invoice invoice = extractInvoiceFromResultSet(rs);
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

// Hàm phụ để tái sử dụng
    private Invoice extractInvoiceFromResultSet(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getInt("id"));
        invoice.setCustomerId(rs.getInt("customer_id"));
        invoice.setUserId(rs.getInt("user_id"));
        invoice.setTotalAmount(rs.getDouble("total_amount"));
        invoice.setPaidAmount(rs.getDouble("paid_amount"));
        invoice.setChangeAmount(rs.getDouble("change_amount"));
        invoice.setNote(rs.getString("note"));
        invoice.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return invoice;
    }

    public List<Invoice> getInvoicesByUserId(int userId) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = extractInvoiceFromResultSet(rs);
                list.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
