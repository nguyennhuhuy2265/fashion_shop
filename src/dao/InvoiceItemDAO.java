package dao;

import model.InvoiceItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceItemDAO {

    public void insertInvoiceItems(List<InvoiceItem> items) {
        String sql = "INSERT INTO invoice_items (invoice_id, product_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (InvoiceItem item : items) {
                stmt.setInt(1, item.getInvoiceId());
                stmt.setInt(2, item.getProductId());
                stmt.setInt(3, item.getQuantity());
                stmt.setInt(4, item.getUnitPrice());
                stmt.setInt(5, item.getTotalPrice());

                stmt.addBatch();
            }
            stmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<InvoiceItem> getItemsByInvoiceId(int invoiceId) {
        List<InvoiceItem> list = new ArrayList<>();
        String sql = "SELECT * FROM invoice_items WHERE invoice_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, invoiceId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                InvoiceItem item = new InvoiceItem();
                item.setId(rs.getInt("id"));
                item.setInvoiceId(rs.getInt("invoice_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getInt("unit_price"));
                item.setTotalPrice(rs.getInt("total_price"));

                list.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm mới để lấy dữ liệu thống kê doanh số theo năm và tháng
    public List<Object[]> getSalesByYearAndMonth(int year, int month) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, SUM(ii.quantity) AS total_quantity "
                + "FROM invoice_items ii "
                + "JOIN invoices i ON ii.invoice_id = i.id "
                + "JOIN products p ON ii.product_id = p.id "
                + "WHERE YEAR(i.created_at) = ? AND MONTH(i.created_at) = ? "
                + "GROUP BY p.id, p.name "
                + "ORDER BY total_quantity DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, year);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[3];
                row[0] = rs.getInt("product_id"); // Mã sản phẩm
                row[1] = rs.getString("product_name"); // Tên sản phẩm
                row[2] = rs.getInt("total_quantity"); // Số lượng bán
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
