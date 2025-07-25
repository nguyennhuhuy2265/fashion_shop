package dao;

import model.Invoice;

import java.sql.*;

public class InvoiceDAO {

    public int insertInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoices (customer_id, user_id, created_at, total_amount, points_used, points_earned) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, invoice.getCustomerId());
            ps.setInt(2, invoice.getUserId());
            ps.setTimestamp(3, Timestamp.valueOf(invoice.getCreatedAt()));
            ps.setBigDecimal(4, invoice.getTotalAmount());
            ps.setInt(5, invoice.getPointsUsed());
            ps.setInt(6, invoice.getPointsEarned());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating invoice failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Trả về ID vừa tạo
                } else {
                    throw new SQLException("Creating invoice failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Có thể thêm các hàm khác như getById, getByCustomerId nếu cần
}
