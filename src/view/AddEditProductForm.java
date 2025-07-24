package view;

import dao.CategoryDAO;
import model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;

public class AddEditProductForm extends javax.swing.JDialog {

    private ActionListener saveListener;
    private ActionListener cancelListener;
    private Map<Integer, String> categoryMap;
    private String selectedImagePath = null;

    public void setSaveListener(ActionListener listener) {
        this.saveListener = listener;
    }

    public void setCancelListener(ActionListener listener) {
        this.cancelListener = listener;
    }

    public AddEditProductForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setSize(430, 420);

        saveButton.addActionListener(e -> {
            if (saveListener != null) {
                saveListener.actionPerformed(e);
            }
        });

        cancelButton.addActionListener(e -> {
            if (cancelListener != null) {
                cancelListener.actionPerformed(e);
            }
        });

        addImageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chooseImage();
            }
        });

        loadCategories(); // load khi khởi tạo
    }

    // ====================== LOAD CATEGORIES ======================
    private void loadCategories() {
        CategoryDAO dao = new CategoryDAO();
        categoryMap = dao.getAllCategories();
        categoryComboBox.removeAllItems();
        for (String name : categoryMap.values()) {
            categoryComboBox.addItem(name);
        }
    }

    // ====================== SET FORM DATA ======================
    public void setProduct(Product product) {
        nameTextField.setText(product.getName());
        priceTextField.setText(String.valueOf(product.getPrice()));
        quantityTextField.setText(String.valueOf(product.getQuantity()));
        sizeTextField.setText(product.getSize());
        colorTextField.setText(product.getColor());
        materialTextField.setText(product.getMaterial());
        descriptionTextArea.setText(product.getDescription());

        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            ImageIcon icon = new ImageIcon(product.getImageUrl());
            imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
            selectedImagePath = product.getImageUrl();
        }

        // Load danh mục và chọn sẵn
        if (product.getCategoryName() != null) {
            categoryComboBox.setSelectedItem(product.getCategoryName());
        }
    }

    // ====================== GET INPUT PRODUCT ======================
    public Product getProductInput() {
        Product product = new Product();
        product.setName(nameTextField.getText().trim());
        product.setPrice(Double.parseDouble(priceTextField.getText().trim()));
        product.setQuantity(Integer.parseInt(quantityTextField.getText().trim()));
        product.setSize(sizeTextField.getText().trim());
        product.setColor(colorTextField.getText().trim());
        product.setMaterial(materialTextField.getText().trim());
        product.setDescription(descriptionTextArea.getText().trim());
        product.setImageUrl(selectedImagePath);

        // Lấy categoryId từ categoryMap
        String selectedCategoryName = (String) categoryComboBox.getSelectedItem();
        for (Map.Entry<Integer, String> entry : categoryMap.entrySet()) {
            if (entry.getValue().equals(selectedCategoryName)) {
                product.setCategoryId(entry.getKey());
                product.setCategoryName(entry.getValue());
                break;
            }
        }

        return product;
    }

    // ====================== IMAGE CHOOSER ======================
    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            selectedImagePath = fileChooser.getSelectedFile().getAbsolutePath();
            ImageIcon icon = new ImageIcon(selectedImagePath);
            imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        }
    }

    public String getSelectedImagePath() {
        return selectedImagePath;
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imageLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        priceTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        sizeTextField = new javax.swing.JTextField();
        colorTextField = new javax.swing.JTextField();
        materialTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        categoryComboBox = new javax.swing.JComboBox<>();
        quantityTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        addImageLabel = new javax.swing.JLabel();

        imageLabel.setBackground(new java.awt.Color(255, 255, 255));
        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\icons8-add-50.png")); // NOI18N

        jLabel1.setText("Tên sản phẩm");

        jLabel2.setText("Giá bán");

        jLabel3.setText("Danh mục");

        jLabel4.setText("Size");

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        jScrollPane1.setViewportView(descriptionTextArea);

        jLabel5.setText("Màu");

        jLabel6.setText("Chất liệu");

        jLabel7.setText("Số lượng");

        categoryComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setText("Mô tả sản phẩm");

        saveButton.setText("Lưu");

        cancelButton.setText("Hủy");

        addImageLabel.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\add.png")); // NOI18N
        addImageLabel.setText("Ảnh");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1)
                        .addGap(219, 219, 219)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(sizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2)
                        .addGap(252, 252, 252)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(colorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel3)
                        .addGap(105, 105, 105)
                        .addComponent(jLabel7)
                        .addGap(86, 86, 86)
                        .addComponent(jLabel6))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(materialTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(246, 246, 246)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jLabel8))))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(addImageLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(materialTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveButton)
                    .addComponent(cancelButton))
                .addContainerGap(15, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addImageLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox<String> categoryComboBox;
    private javax.swing.JTextField colorTextField;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField materialTextField;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField priceTextField;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField sizeTextField;
    // End of variables declaration//GEN-END:variables
}
