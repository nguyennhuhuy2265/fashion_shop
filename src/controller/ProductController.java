package controller;

import dao.ProductDAO;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductController {
    private final ProductDAO productDAO = new ProductDAO();

    public void loadProductTable(JTable table) {
        List<Product> productList = productDAO.getAllProducts();
        updateTableModel(table, productList);
    }

    public void searchAndFilterProducts(JTable table, String keyword, int categoryId) {
        List<Product> productList = productDAO.searchProduct(keyword, categoryId);
        updateTableModel(table, productList);
    }

    public void deleteSelectedProduct(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một sản phẩm để xóa.");
            return;
        }

        int productId = (int) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = productDAO.deleteProduct(productId);
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Xóa sản phẩm thành công.");
                loadProductTable(table);
            } else {
                JOptionPane.showMessageDialog(null, "Xóa sản phẩm thất bại.");
            }
        }
    }

    private void updateTableModel(JTable table, List<Product> productList) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Product p : productList) {
            model.addRow(new Object[]{
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getSize(),
                p.getColor(),
                p.getDescription(),
                p.getQuantity(),
                p.getCategoryName()
            });
        }
    }
}
