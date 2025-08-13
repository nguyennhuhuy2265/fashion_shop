package controller;

import dao.CategoryDAO;
import dao.ProductDAO;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;
import model.Category;
import view.admin.EditProductDialog;

public class ProductController {

    private final ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

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
                JOptionPane.showMessageDialog(null, "Sản phẩm đã đưa vào kinh doanh không thể xóa.");
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
                FormatUtil.formatCurrency(p.getPrice()),
                p.getSize(),
                p.getColor(),
                p.getMaterial(),
                p.getQuantity(),
                p.getCategoryName()
            });
        }
    }

    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }

    public void addProduct(Product p) {
        productDAO.insertProduct(p);
    }

    public void updateProduct(Product p) {
        productDAO.updateProduct(p);
    }

    public void showAddProductDialog() {
        EditProductDialog form = new EditProductDialog(null, true); // true = modal

        form.setSaveListener(e -> {
            try {
                Product newProduct = form.getProductInput();
                productDAO.insertProduct(newProduct);
                JOptionPane.showMessageDialog(null, "Thêm sản phẩm thành công.");
                form.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi thêm sản phẩm.");
            }
        });

        form.setCancelListener(e -> form.dispose());
        form.setTitle("Thêm Sản Phẩm");
        form.setLocationRelativeTo(null);
        form.setVisible(true);
    }

    public void showEditProductDialog(int productId) {
        Product existingProduct = productDAO.getProductById(productId);
        if (existingProduct == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sản phẩm.");
            return;
        }

        EditProductDialog form = new EditProductDialog(null, true);
        form.setProduct(existingProduct);

        form.setSaveListener(e -> {
            try {
                Product updatedProduct = form.getProductInput();
                updatedProduct.setId(productId); // giữ nguyên id
                productDAO.updateProduct(updatedProduct);
                JOptionPane.showMessageDialog(null, "Cập nhật sản phẩm thành công.");
                form.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật sản phẩm.");
            }
        });

        form.setCancelListener(e -> form.dispose());
        form.setTitle("Chỉnh Sửa Sản Phẩm");
        form.setLocationRelativeTo(null);
        form.setVisible(true);
    }

}
