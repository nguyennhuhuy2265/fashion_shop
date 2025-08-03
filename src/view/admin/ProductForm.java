package view.admin;

import controller.ProductController;
import dao.CategoryDAO;
import java.awt.event.ItemEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Product;

public class ProductForm extends javax.swing.JPanel {

    private ProductController productController = new ProductController();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private Map<Integer, String> categoryMap = new LinkedHashMap<>();

    public ProductForm() {
        initComponents();
        initData();            // Load table + comboBox
        initEventHandlers();   // Gán các sự kiện

    }

    // Load dữ liệu ban đầu
    private void initData() {
        productController.loadProductTable(productTable);
        loadCategoryComboBox();
    }

    // Load danh mục vào ComboBox
    private void loadCategoryComboBox() {
        categoryMap.clear();
        categoryMap.put(-1, "Tất cả");  // Tùy chọn "Tất cả"
        categoryMap.putAll(categoryDAO.getAllCategories());

        categoryComboBox.removeAllItems();
        for (String name : categoryMap.values()) {
            categoryComboBox.addItem(name);
        }
    }

    // Xử lý các sự kiện nút và combo box
    private void initEventHandlers() {
        // Tìm kiếm
        searchButton.addActionListener(e -> performSearch());

        // Xóa
        deleteButton.addActionListener(e -> productController.deleteSelectedProduct(productTable));

        //Thêm
        addButton.addActionListener(e -> {
            productController.showAddProductDialog();
            productController.loadProductTable(productTable); // refresh sau khi thêm
        });

        //Sửa
        editButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                int productId = (int) productTable.getValueAt(selectedRow, 0);
                productController.showEditProductDialog(productId);
                productController.loadProductTable(productTable); // refresh lại bảng
            } else {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm để sửa.");
            }
        });

        // Lọc danh mục
        categoryComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                performSearch(); // Tự động lọc khi chọn danh mục
            }
        });
    }

    // Tìm kiếm và lọc sản phẩm
    private void performSearch() {
        String keyword = searchProductTextField.getText().trim();
        int selectedIndex = categoryComboBox.getSelectedIndex();
        Integer categoryId = (Integer) categoryMap.keySet().toArray()[selectedIndex];

        productController.searchAndFilterProducts(productTable, keyword, categoryId);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        searchProductTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        categoryComboBox = new javax.swing.JComboBox<>();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Giá", "Size", "Màu sắc", "Chất liệu", "Số lượng tồn kho"
            }
        ));
        jScrollPane1.setViewportView(productTable);

        searchButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\search.png")); // NOI18N

        categoryComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        addButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\add.png")); // NOI18N
        addButton.setText("Thêm");

        editButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\edit.png")); // NOI18N
        editButton.setText("Sửa");

        deleteButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\delete.png")); // NOI18N
        deleteButton.setText("Xóa");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchProductTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchButton)
                        .addGap(32, 32, 32)
                        .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                        .addComponent(addButton)
                        .addGap(18, 18, 18)
                        .addComponent(editButton)
                        .addGap(18, 18, 18)
                        .addComponent(deleteButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchButton)
                    .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addButton)
                    .addComponent(editButton)
                    .addComponent(deleteButton)
                    .addComponent(searchProductTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JComboBox<String> categoryComboBox;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable productTable;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchProductTextField;
    // End of variables declaration//GEN-END:variables
}
