package view.admin;

import view.staff.*;
import dao.CustomerDAO;
import dao.InvoiceDAO;
import dao.UserDAO;
import java.awt.Frame;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.Invoice;
import model.User;

public class InvoiceForm extends javax.swing.JPanel {

    private InvoiceDAO invoiceDAO = new InvoiceDAO();

    public InvoiceForm() {
        initComponents();
        setupListeners();
        populateStaffComboBox();
        loadAllInvoices();
        viewDetailButton.addActionListener(e -> showInvoiceDetail());

    }
    private void populateStaffComboBox() {
    UserDAO userDAO = new UserDAO();
    List<User> staffList = userDAO.getAllStaffUsers();
    
    staffListComboBox.removeAllItems();
    staffListComboBox.addItem("Tất cả");  // Option mặc định

    for (User staff : staffList) {
        staffListComboBox.addItem(staff.getId() + " - " + staff.getFullname());
    }
}


    private void showInvoiceDetail() {
        int selectedRow = invoiceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xem.");
            return;
        }

        int invoiceId = (int) invoiceTable.getValueAt(selectedRow, 0);

        Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
        if (invoice == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn.");
            return;
        }

        // Lấy thông tin khách hàng và nhân viên
        CustomerDAO customerDAO = new CustomerDAO();
        UserDAO userDAO = new UserDAO();

        String customerName = customerDAO.getCustomerNameById(invoice.getCustomerId());
        String customerPhone = customerDAO.getCustomerPhoneById(invoice.getCustomerId());
        String staffName = userDAO.getUserFullName(invoice.getUserId());

        // Mở dialog
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        InvoiceDetailDialog dialog = new InvoiceDetailDialog(parentFrame, invoice, customerName, customerPhone, staffName);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void setupListeners() {
        searchButton.addActionListener(e -> searchInvoices());
        filterButton.addActionListener(e -> filterInvoicesByDate());
        todayButton.addActionListener(e -> filterTodayInvoices());
        staffListComboBox.addActionListener(e -> filterInvoicesBySelectedStaff());

    }
    private void filterInvoicesBySelectedStaff() {
    String selected = (String) staffListComboBox.getSelectedItem();
    if (selected == null || selected.equals("Tất cả")) {
        loadAllInvoices();  // nếu chọn tất cả thì load toàn bộ
        return;
    }

    try {
        int staffId = Integer.parseInt(selected.split(" - ")[0].trim());
        List<Invoice> filteredInvoices = invoiceDAO.getInvoicesByUserId(staffId);
        loadInvoicesToTable(filteredInvoices);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Lỗi khi lấy ID nhân viên.");
    }
}

    

    private void searchInvoices() {
        String keyword = searchTextField.getText().trim();
        if (!keyword.isEmpty()) {
            List<Invoice> list = invoiceDAO.searchInvoiceById(keyword);
            loadInvoicesToTable(list);
        }
    }

    private void filterInvoicesByDate() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime start = LocalDate.parse(startDateTextField.getText().trim(), formatter).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDateTextField.getText().trim(), formatter).atTime(23, 59, 59);
            List<Invoice> list = invoiceDAO.getInvoicesByDateRange(start, end);
            loadInvoicesToTable(list);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ (dd-MM-yyyy)");
        }
    }

    private void filterTodayInvoices() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Cập nhật ô nhập ngày
        startDateTextField.setText(today.format(formatter));
        endDateTextField.setText(today.format(formatter));
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);
        List<Invoice> list = invoiceDAO.getInvoicesByDateRange(start, end);
        loadInvoicesToTable(list);
    }

    private void loadAllInvoices() {
        List<Invoice> list = invoiceDAO.getAllInvoices();
        loadInvoicesToTable(list);
    }

    private void loadInvoicesToTable(List<Invoice> invoices) {
        DefaultTableModel model = (DefaultTableModel) invoiceTable.getModel();
        model.setRowCount(0); // clear table

        CustomerDAO customerDAO = new CustomerDAO();
        UserDAO userDAO = new UserDAO();

        for (Invoice inv : invoices) {
            String customerName = customerDAO.getCustomerNameById(inv.getCustomerId());
            String customerPhone = customerDAO.getCustomerPhoneById(inv.getCustomerId());
            String employee = userDAO.getUserFullName(inv.getUserId());

            model.addRow(new Object[]{
                inv.getId(),
                customerName,
                customerPhone,
                employee,
                inv.getNote(),
                inv.getTotalAmount(),
                inv.getCreatedAt().toLocalDate()
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        invoiceTable = new javax.swing.JTable();
        searchTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        startDateTextField = new javax.swing.JTextField();
        endDateTextField = new javax.swing.JTextField();
        filterButton = new javax.swing.JButton();
        todayButton = new javax.swing.JButton();
        viewDetailButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        staffListComboBox = new javax.swing.JComboBox<>();

        invoiceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hóa đơn", "Tên khách hàng", "Số điện thoại", "Nhân viên", "Ghi chú", "Thành tiền", "Ngày tạo"
            }
        ));
        jScrollPane1.setViewportView(invoiceTable);

        searchButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\search.png")); // NOI18N

        jLabel1.setText("Tìm theo mã hóa đơn");

        jLabel2.setText("Từ ngày");

        jLabel3.setText("đến ngày");

        startDateTextField.setText("01-07-2025");

        endDateTextField.setText("30-07-2025");

        filterButton.setText("Lọc");

        todayButton.setText("Hôm nay");

        viewDetailButton.setText("Xem chi tiết");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Hóa đơn bán hàng");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(startDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(filterButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(todayButton)))
                        .addGap(0, 24, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 836, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(staffListComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(viewDetailButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22))
            .addGroup(layout.createSequentialGroup()
                .addGap(360, 360, 360)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel4)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(viewDetailButton, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(staffListComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(startDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(endDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(filterButton)
                            .addComponent(todayButton)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField endDateTextField;
    private javax.swing.JButton filterButton;
    private javax.swing.JTable invoiceTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JComboBox<String> staffListComboBox;
    private javax.swing.JTextField startDateTextField;
    private javax.swing.JButton todayButton;
    private javax.swing.JButton viewDetailButton;
    // End of variables declaration//GEN-END:variables
}
