package view.admin;

import dao.ImportItemDAO;
import dao.ImportReceiptDAO;
import dao.ProductDAO;
import dao.SupplierDAO;
import dao.UserDAO;
import java.awt.Frame;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.ImportItem;
import model.ImportReceipt;
import model.Product;
import model.Supplier;

public class ImportForm extends javax.swing.JPanel {

    public ImportForm() {
        initComponents();
        setupEventHandlers();
        loadSuppliers();

    }

    private void setupEventHandlers() {
        ProductDAO productDAO = new ProductDAO();

        // Hiển thị danh sách sản phẩm khi mở form
        loadProductTable(productDAO.getAllProducts());

        // Tìm kiếm sản phẩm theo tên
        searchButton.addActionListener(e -> {
            String keyword = searchTextField.getText().trim();
            List<Product> results = productDAO.searchProductByName(keyword);
            loadProductTable(results);
        });
        // Thêm nahf cung cấp
        addSupplierButton.addActionListener(e -> {
            EditSupplierDialog dialog = new EditSupplierDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                loadSuppliers(); // Tải lại danh sách nếu đã thêm mới
            }
        });
        saveButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                showMessage("Vui lòng chọn một sản phẩm từ bảng!");
                return;
            }

            try {
                DefaultTableModel sourceModel = (DefaultTableModel) productTable.getModel();
                int productId = (int) sourceModel.getValueAt(selectedRow, 0);
                String productName = (String) sourceModel.getValueAt(selectedRow, 1);
                String size = (String) sourceModel.getValueAt(selectedRow, 2);
                String color = (String) sourceModel.getValueAt(selectedRow, 3);
                String material = (String) sourceModel.getValueAt(selectedRow, 4);

                int quantity = Integer.parseInt(quantityTextField.getText().trim());
                int importPrice = Integer.parseInt(importPriceTextField.getText().trim());

                DefaultTableModel tempModel = (DefaultTableModel) productTable1.getModel();
                tempModel.addRow(new Object[]{productId, productName, size, color, material, importPrice, quantity});
                updateTotalPrice();  // Cập nhật tổng tiền sau khi thêm
                clearInputFields(); // xóa dữ liệu nhập sau khi thêm
            } catch (Exception ex) {
                showMessage("Vui lòng nhập đúng số lượng và giá nhập.");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = productTable1.getSelectedRow();
            if (selectedRow != -1) {
                DefaultTableModel tempModel = (DefaultTableModel) productTable1.getModel();
                tempModel.removeRow(selectedRow);
                updateTotalPrice();  // Cập nhật tổng tiền sau khi thêm
                clearInputFields(); // xóa dữ liệu nhập sau khi thêm
            } else {
                showMessage("Vui lòng chọn sản phẩm cần xóa khỏi danh sách tạm.");
            }
        });
        submitButton.addActionListener(e -> {
            Supplier selectedSupplier = (Supplier) suppliersListComboBox.getSelectedItem();
            String note = noteTextArea.getText().trim();
            DefaultTableModel model = (DefaultTableModel) productTable1.getModel();

            if (model.getRowCount() == 0) {
                showMessage("Danh sách sản phẩm nhập đang trống.");
                return;
            }

            if (selectedSupplier == null) {
                showMessage("Vui lòng chọn nhà cung cấp.");
                return;
            }

            try {
                ImportReceipt receipt = new ImportReceipt();
                receipt.setSupplierId(selectedSupplier.getId());
                receipt.setUserId(UserDAO.getCurrentUser().getId());
                receipt.setNote(note);
                receipt.setCreatedAt(LocalDateTime.now());

                int totalAmount = 0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    int quantity = (int) model.getValueAt(i, 6);
                    int unitPrice = (int) model.getValueAt(i, 5);
                    totalAmount += quantity * unitPrice;
                }
                receipt.setTotalAmount(totalAmount);

                ImportReceiptDAO receiptDAO = new ImportReceiptDAO();
                int receiptId = receiptDAO.insertReceipt(receipt);

                if (receiptId == -1) {
                    showMessage("Không thể lưu phiếu nhập.");
                    return;
                }

                List<ImportItem> itemList = new ArrayList<>();

                for (int i = 0; i < model.getRowCount(); i++) {
                    int productId = (int) model.getValueAt(i, 0);
                    int unitPrice = (int) model.getValueAt(i, 5);
                    int quantity = (int) model.getValueAt(i, 6);
                    int totalPrice = quantity * unitPrice;

                    ImportItem item = new ImportItem();
                    item.setImportReceiptId(receiptId);
                    item.setProductId(productId);
                    item.setUnitPrice(unitPrice);
                    item.setQuantity(quantity);
                    item.setTotalPrice(totalPrice);

                    itemList.add(item);

                    // Gọi hàm tăng số lượng tồn kho
                    productDAO.increaseQuantityById(productId, quantity);
                }

                ImportItemDAO itemDAO = new ImportItemDAO();
                boolean success = itemDAO.insertImportItems(itemList);

                if (success) {
                    model.setRowCount(0);
                    showMessage("Đã nhập hàng thành công!");
                    loadProductTable(productDAO.getAllProducts());
                } else {
                    showMessage("Lỗi khi lưu chi tiết sản phẩm nhập.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                showMessage("Lỗi khi lưu phiếu nhập: " + ex.getMessage());
            }
        });

    }

    private void clearInputFields() {
        quantityTextField.setText("");
        importPriceTextField.setText("");
        noteTextArea.setText("");
        productTable.clearSelection();
    }

    private void showMessage(String message) {
        javax.swing.JOptionPane.showMessageDialog(this, message);
    }

    private void loadSuppliers() {
        SupplierDAO supplierDAO = new SupplierDAO();
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        suppliersListComboBox.removeAllItems();
        for (Supplier s : suppliers) {
            suppliersListComboBox.addItem(s);
        }
    }

    private void loadProductTable(List<Product> products) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0); // Clear cũ

        for (Product p : products) {
            model.addRow(new Object[]{
                p.getId(),
                p.getName(),
                p.getSize(),
                p.getColor(),
                p.getMaterial(),
                p.getPrice(),
                p.getQuantity()
            });
        }
    }
private void updateTotalPrice() {
    DefaultTableModel tempModel = (DefaultTableModel) productTable1.getModel();
    int total = 0;
    for (int i = 0; i < tempModel.getRowCount(); i++) {
        int price = (int) tempModel.getValueAt(i, 5);     // Giá nhập
        int quantity = (int) tempModel.getValueAt(i, 6);  // Số lượng nhập
        total += price * quantity;
    }
    totalPriceTextField.setText(String.valueOf(total));
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        searchTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        quantityTextField = new javax.swing.JTextField();
        importPriceTextField = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        productTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        noteTextArea = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        submitButton = new javax.swing.JButton();
        suppliersListComboBox = new javax.swing.JComboBox<>();
        addSupplierButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        totalPriceTextField = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(949, 680));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Danh sách sản phẩm");

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Size", "Màu sắc", "Chất liệu", "Giá bán", "Số lượng tồn kho"
            }
        ));
        jScrollPane1.setViewportView(productTable);

        searchButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\search.png")); // NOI18N

        jLabel2.setText("Số lượng");

        jLabel3.setText("Giá nhập");

        saveButton.setText("Lưu tạm");

        deleteButton.setText("Xóa tạm");

        productTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Size", "Màu sắc", "Chất liệu", "Giá nhập", "Số lượng nhập"
            }
        ));
        jScrollPane2.setViewportView(productTable1);

        jLabel4.setText("Nhà  cung cấp");

        noteTextArea.setColumns(20);
        noteTextArea.setRows(5);
        jScrollPane3.setViewportView(noteTextArea);

        jLabel6.setText("Ghi chú");

        submitButton.setText("Xác nhận");

        addSupplierButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\add.png")); // NOI18N

        jLabel5.setText("Tổng tiền");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(searchButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(importPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saveButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteButton)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 769, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(submitButton))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(totalPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel6)
                                                    .addComponent(jLabel4)
                                                    .addComponent(suppliersListComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(addSupplierButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(importPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveButton)
                    .addComponent(deleteButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(suppliersListComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addSupplierButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(totalPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addComponent(submitButton))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 31, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addSupplierButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JTextField importPriceTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea noteTextArea;
    private javax.swing.JTable productTable;
    private javax.swing.JTable productTable1;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JButton submitButton;
    private javax.swing.JComboBox<Supplier> suppliersListComboBox;
    private javax.swing.JTextField totalPriceTextField;
    // End of variables declaration//GEN-END:variables

}
