package view.staff;

import dao.CustomerDAO;
import dao.InvoiceDAO;
import dao.InvoiceItemDAO;
import dao.ProductDAO;
import dao.UserDAO;
import java.awt.Dimension;
import java.awt.Frame;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.Customer;
import model.Invoice;
import model.InvoiceItem;
import model.Product;
import model.User;

public class SellForm extends javax.swing.JPanel {

    User current = UserDAO.getCurrentUser();

    public SellForm() {
        initComponents();
        setupEventHandlers();
        loadCustomers();

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
        pointCheckBox.addActionListener(e -> updateTotalAmount());
        paidAmountTextField.addActionListener(e -> updateChangeAmount());
        paidAmountTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updateChangeAmount();
            }
        });
        addCustomerButton.addActionListener(e -> {
            EditCustomerDialog dialog = new EditCustomerDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                loadCustomers(); // Tải lại danh sách nếu đã thêm mới
            }
        });

        submitButton.addActionListener(e -> saveInvoice());
        deleteButton.addActionListener(e -> {
            int selectedRow = productSellTable.getSelectedRow();
            if (selectedRow != -1) {
                DefaultTableModel model = (DefaultTableModel) productSellTable.getModel();
                model.removeRow(selectedRow);
                updateTotalAmount();  // Cập nhật lại tổng tiền
            }
        });

        saveButton.addActionListener(e -> addSelectedProductToSellTable());

    }

    private void saveInvoice() {
        try {
            // 1. Lấy thông tin khách hàng được chọn
            Customer selectedCustomer = (Customer) customersListComboBox.getSelectedItem();
            if (selectedCustomer == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng.");
                return;
            }

            // 2. Lấy danh sách sản phẩm đang bán
            DefaultTableModel model = (DefaultTableModel) productSellTable.getModel();
            List<InvoiceItem> items = new ArrayList<>();
            double total = 0;

            for (int i = 0; i < model.getRowCount(); i++) {
                int productId = Integer.parseInt(model.getValueAt(i, 0).toString());
                String name = model.getValueAt(i, 1).toString();
                double unitPrice = Double.parseDouble(model.getValueAt(i, 5).toString());
                int quantity = Integer.parseInt(model.getValueAt(i, 6).toString());

                double totalPrice = unitPrice * quantity;
                total += totalPrice;

                InvoiceItem item = new InvoiceItem();
                item.setProductId(productId);
                item.setQuantity(quantity);
                item.setUnitPrice(unitPrice);
                item.setTotalPrice(totalPrice);
                items.add(item);
            }

            int point = selectedCustomer.getPoints();
            // 3. Tính tiền thanh toán
            if (pointCheckBox.isSelected()) {
                total = Math.max(0, total - point * 100); // Trừ điểm giả định
                selectedCustomer.setPoints(0);
                new CustomerDAO().updateCustomerPoints(selectedCustomer.getId(), 0);
            }

            double paid = Double.parseDouble(paidAmountTextField.getText().trim());
            double change = paid - total;

            // 4. Tạo hoá đơn
            Invoice invoice = new Invoice();
            invoice.setCustomerId(selectedCustomer.getId());
            invoice.setUserId(current.getId());
            invoice.setTotalAmount(total);
            invoice.setPaidAmount(paid);
            invoice.setChangeAmount(change > 0 ? change : 0);
            invoice.setNote(noteTextArea.getText());
            invoice.setCreatedAt(LocalDateTime.now());

            // 5. Lưu vào DB
            InvoiceDAO invoiceDAO = new InvoiceDAO();
            int invoiceId = invoiceDAO.insertInvoice(invoice);
            // 6. Cộng điểm thưởng cho khách
            if (invoiceId > 0) {
                for (InvoiceItem item : items) {
                    item.setInvoiceId(invoiceId);
                }
                InvoiceItemDAO invoiceItemDAO = new InvoiceItemDAO();
                invoiceItemDAO.insertInvoiceItems(items);

                // Tính điểm: mỗi 100.000 VND = +1 điểm
                int addedPoints = (int) (invoice.getTotalAmount() / 100000);

                // Không reset điểm sau khi dùng, chỉ cộng thêm
                int newPointBalance = selectedCustomer.getPoints() + addedPoints;

                selectedCustomer.setPoints(newPointBalance);
                new CustomerDAO().updateCustomerPoints(selectedCustomer.getId(), newPointBalance);

                JOptionPane.showMessageDialog(this, "Tạo hoá đơn thành công.");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Tạo hoá đơn thất bại.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tạo hoá đơn.");
        }
    }

    private void clearForm() {
        DefaultTableModel model = (DefaultTableModel) productSellTable.getModel();
        model.setRowCount(0);

        totalAmountTextField.setText("");
        paidAmountTextField.setText("");
        changeAmountTextField.setText("");
        noteTextArea.setText("");
        pointCheckBox.setSelected(false);
    }

    private void loadCustomers() {
        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> customers = customerDAO.getAllCustomers();
        customersListComboBox.removeAllItems();
        for (Customer c : customers) {
            customersListComboBox.addItem(c);
        }
    }

    private void addSelectedProductToSellTable() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityTextField.getText().trim());
            if (quantity <= 0) {
                return;
            }

            DefaultTableModel model = (DefaultTableModel) productSellTable.getModel();

            Object[] rowData = new Object[7];
            for (int i = 0; i < 6; i++) {
                rowData[i] = productTable.getValueAt(selectedRow, i);
            }
            rowData[6] = quantity; // Số lượng bán

            model.addRow(rowData);
            updateTotalAmount(); // Tự tính lại tổng tiền
            quantityTextField.setText("");  // Reset ô nhập số lượng
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void updateTotalAmount() {
        DefaultTableModel model = (DefaultTableModel) productSellTable.getModel();
        double total = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            double price = Double.parseDouble(model.getValueAt(i, 5).toString());
            int quantity = Integer.parseInt(model.getValueAt(i, 6).toString());
            total += price * quantity;
        }

        if (pointCheckBox.isSelected()) {
            Customer selectedCustomer = (Customer) customersListComboBox.getSelectedItem();
            if (selectedCustomer != null) {
                int points = selectedCustomer.getPoints();

                // Xác định % giảm theo điểm
                int discountPercent = 0;
                if (points >= 50) {
                    discountPercent = 15;
                } else if (points >= 30) {
                    discountPercent = 10;
                } else if (points >= 20) {
                    discountPercent = 7;
                } else if (points >= 10) {
                    discountPercent = 5;
                } else if (points > 0) {
                    discountPercent = 2;
                }

                double discountAmount = total * discountPercent / 100.0;
                total = total - discountAmount;
            }
        }

        totalAmountTextField.setText(String.valueOf(total));
    }

    private void updateChangeAmount() {
        try {
            double paid = Double.parseDouble(paidAmountTextField.getText().trim());
            double total = Double.parseDouble(totalAmountTextField.getText().trim());

            double change = paid - total;
            changeAmountTextField.setText(String.valueOf(change < 0 ? 0 : change));
        } catch (NumberFormatException e) {
            e.printStackTrace();
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        searchTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        quantityTextField = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        productSellTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        noteTextArea = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        submitButton = new javax.swing.JButton();
        customersListComboBox = new javax.swing.JComboBox<>();
        addCustomerButton = new javax.swing.JButton();
        pointCheckBox = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        totalAmountTextField = new javax.swing.JTextField();
        changeAmountTextField = new javax.swing.JTextField();
        paidAmountTextField = new javax.swing.JTextField();
        deleteButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1090, 670));

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

        saveButton.setText("Lưu tạm");

        productSellTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Size", "Màu sắc", "Chất liệu", "Đơn giá", "Số lượng "
            }
        ));
        jScrollPane2.setViewportView(productSellTable);

        jLabel4.setText("Thông tin khách hàng");

        noteTextArea.setColumns(20);
        noteTextArea.setRows(5);
        jScrollPane3.setViewportView(noteTextArea);

        jLabel6.setText("Ghi chú");

        submitButton.setText("Xác nhận");

        addCustomerButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\add.png")); // NOI18N

        pointCheckBox.setText("Đổi điểm tích lũy");

        jLabel3.setText("Tổng tiền");

        jLabel5.setText("Tiền thừa");

        jLabel7.setText("Tiền khách đưa");

        deleteButton.setText("Xóa");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
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
                                .addGap(17, 17, 17)
                                .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(saveButton)
                                .addGap(18, 18, 18)
                                .addComponent(deleteButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 763, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(changeAmountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(submitButton)
                                        .addGap(27, 27, 27))
                                    .addComponent(jLabel6)
                                    .addComponent(pointCheckBox)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jScrollPane3)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(customersListComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(addCustomerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(totalAmountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel3))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel7)
                                                .addGap(54, 54, 54))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(paidAmountTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)))))))
                        .addGap(13, 13, 13))))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(saveButton)
                        .addComponent(deleteButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customersListComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addCustomerButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pointCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7))
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalAmountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(paidAmountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(changeAmountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(submitButton)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCustomerButton;
    private javax.swing.JTextField changeAmountTextField;
    private javax.swing.JComboBox<Customer> customersListComboBox;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea noteTextArea;
    private javax.swing.JTextField paidAmountTextField;
    private javax.swing.JCheckBox pointCheckBox;
    private javax.swing.JTable productSellTable;
    private javax.swing.JTable productTable;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JButton submitButton;
    private javax.swing.JTextField totalAmountTextField;
    // End of variables declaration//GEN-END:variables
}
