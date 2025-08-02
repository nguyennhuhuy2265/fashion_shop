package dao;

import model.ImportItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class ImportItemDAO {

    public boolean insertImportItems(List<ImportItem> items) {
        String sql = "INSERT INTO import_items (import_receipt_id, product_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
}
