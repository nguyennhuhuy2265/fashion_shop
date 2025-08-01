package view.staff;

import view.admin.*;
import dao.CustomerDAO;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Customer;

public class CustomerForm extends javax.swing.JPanel {

    private CustomerDAO customerDAO = new CustomerDAO();
    private Customer selectedCustomer = null;

    public CustomerForm() {
        initComponents();
        loadCustomerTable();
        setupListeners();
    }

    private void loadCustomerTable() {
        List<Customer> customers = customerDAO.getAllCustomers();
        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
        model.setRowCount(0);
        for (Customer c : customers) {
            model.addRow(new Object[]{
                c.getId(), c.getName(), c.getPhone(), c.getPoints()
            });
        }
    }

    private void setupListeners() {
        customerTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = customerTable.getSelectedRow();
                if (row != -1) {
                    int id = (int) customerTable.getValueAt(row, 0);
                    selectedCustomer = customerDAO.getCustomerById(id);
                    if (selectedCustomer != null) {
                        nameTextField.setText(selectedCustomer.getName());
                        phoneTextField.setText(selectedCustomer.getPhone());
                        pointLabel.setText(String.valueOf(selectedCustomer.getPoints()));
                    }
                }
            }
        });

        addButton.addActionListener(e -> {
            clearFields();
            selectedCustomer = null;
            updateButton.setText("Thêm mới");
        });

        updateButton.addActionListener(e -> {
            String name = nameTextField.getText().trim();
            String phone = phoneTextField.getText().trim();
            String pointStr = pointLabel.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || pointStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.");
                return;
            }

            int points;
            try {
                points = Integer.parseInt(pointStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Điểm tích lũy phải là số.");
                return;
            }

            if ("Thêm mới".equals(updateButton.getText())) {
                // Chế độ thêm mới
                Customer newCustomer = new Customer(0, name, phone, points);
                if (customerDAO.insertCustomer(newCustomer)) {
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                    loadCustomerTable();
                    clearFields();
                    updateButton.setText("Cập nhật");
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại.");
                }
            } else {
                // Chế độ cập nhật
                if (selectedCustomer != null) {
                    selectedCustomer.setName(name);
                    selectedCustomer.setPhone(phone);
                    selectedCustomer.setPoints(points);
                    if (customerDAO.updateCustomer(selectedCustomer)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                        loadCustomerTable();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng để cập nhật.");
                }
            }
        });

        deleteButton.addActionListener(e -> {
            if (selectedCustomer != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Xóa khách hàng?");
                if (confirm == JOptionPane.YES_OPTION) {
                    if (customerDAO.deleteCustomer(selectedCustomer.getId())) {
                        JOptionPane.showMessageDialog(this, "Đã xóa");
                        loadCustomerTable();
                        clearFields();
                    }
                }
            }
        });

        searchButton.addActionListener(e -> {
            String keyword = searchTextField.getText().trim();
            List<Customer> results = customerDAO.searchCustomers(keyword);
            loadCustomerList(results);
        });
    }

    private void loadCustomerList(List<Customer> customers) {
        DefaultTableModel model = (DefaultTableModel) customerTable.getModel();
        model.setRowCount(0);
        for (Customer c : customers) {
            model.addRow(new Object[]{c.getId(), c.getName(), c.getPhone(), c.getPoints()});
        }
    }

    private void clearFields() {
    nameTextField.setText("");
    phoneTextField.setText("");
    pointLabel.setText("0");
    selectedCustomer = null;
    updateButton.setText("Cập nhật");
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        customerTable = new javax.swing.JTable();
        searchTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        phoneTextField = new javax.swing.JTextField();
        updateButton = new javax.swing.JButton();
        pointLabel = new javax.swing.JLabel();
        deleteButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        customerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Điểm tích lũy"
            }
        ));
        jScrollPane1.setViewportView(customerTable);

        searchButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\search.png")); // NOI18N

        addButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\add.png")); // NOI18N
        addButton.setText("Thêm ");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Tên khách hàng");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Số điện thoại");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Điểm tích lũy");

        nameTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        phoneTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        updateButton.setText("Cập nhật");

        pointLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(phoneTextField)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pointLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 131, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(phoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(pointLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        deleteButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\delete.png")); // NOI18N
        deleteButton.setText("Xóa");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Thông tin khách hàng");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchTextField)
                        .addGap(18, 18, 18)
                        .addComponent(searchButton)
                        .addGap(65, 65, 65)
                        .addComponent(addButton)
                        .addGap(30, 30, 30)
                        .addComponent(deleteButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 62, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addButton)
                        .addComponent(deleteButton)
                        .addComponent(jLabel1)))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JTable customerTable;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField phoneTextField;
    private javax.swing.JLabel pointLabel;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
