package dao;

import model.ImportItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImportItemDAO {

    public boolean insertImportItems(List<ImportItem> items) {
        String sql = "INSERT INTO import_items (import_receipt_id, product_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (ImportItem item : items) {
                stmt.setInt(1, item.getImportReceiptId());
                stmt.setInt(2, item.getProductId());
                stmt.setInt(3, item.getQuantity());
                stmt.setDouble(4, item.getUnitPrice());
                stmt.setDouble(5, item.getTotalPrice());
                stmt.addBatch();
            }
            stmt.executeBatch();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ImportItem> getItemsByImportReceiptId(int importReceiptId) {
        List<ImportItem> list = new ArrayList<>();
        String sql = "SELECT * FROM import_items WHERE import_receipt_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, importReceiptId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ImportItem item = new ImportItem();
                item.setId(rs.getInt("id"));
                item.setImportReceiptId(rs.getInt("import_receipt_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getDouble("unit_price"));
                item.setTotalPrice(rs.getDouble("total_price"));
                list.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
