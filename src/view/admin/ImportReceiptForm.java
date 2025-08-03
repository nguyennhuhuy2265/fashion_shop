package view.admin;

import dao.CustomerDAO;
import dao.ImportReceiptDAO;
import dao.InvoiceDAO;
import dao.SupplierDAO;
import dao.UserDAO;
import java.awt.Frame;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.ImportReceipt;
import model.Invoice;
import model.Supplier;
import view.staff.InvoiceDetailDialog;

public class ImportReceiptForm extends javax.swing.JPanel {

    private ImportReceiptDAO importReceiptDAO = new ImportReceiptDAO();

    public ImportReceiptForm() {
        initComponents();
        setupListeners();
        populateSupplierComboBox();
        loadAllImportReceipts();
        viewDetailButton.addActionListener(e -> showImportReceiptDetail());

    }

    private void populateSupplierComboBox() {
        SupplierDAO supplierDAO = new SupplierDAO();
        List<Supplier> supplierList = supplierDAO.getAllSuppliers();

        supplierListComboBox.removeAllItems();
        supplierListComboBox.addItem("Tất cả");  // Option mặc định

        for (Supplier s : supplierList) {
            supplierListComboBox.addItem(s.getId() + " - " + s.getName());
        }
    }

    private void showImportReceiptDetail() {
        int selectedRow = importReceiptTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xem.");
            return;
        }

        int importReceiptId = (int) importReceiptTable.getValueAt(selectedRow, 0);

        ImportReceipt importReceipt = importReceiptDAO.getReceiptById(importReceiptId);
        if (importReceipt == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn.");
            return;
        }

        // Lấy thông tin khách hàng và nhân viên
        SupplierDAO supplierDAO = new SupplierDAO();
        UserDAO userDAO = new UserDAO();

        String supplierName = supplierDAO.getSupplierNameById(importReceipt.getSupplierId());
        String supplierPhone = supplierDAO.getSupplierPhoneById(importReceipt.getSupplierId());
        String supplierEmail = supplierDAO.getSupplierEmailById(importReceipt.getSupplierId());
                String supplierAddress = supplierDAO.getSupplierAddressById(importReceipt.getSupplierId());
        String staffName = userDAO.getUserFullName(importReceipt.getUserId());

        // Mở dialog
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        ImportReceiptDetailDialog dialog = new ImportReceiptDetailDialog(parentFrame, importReceipt, supplierName, supplierPhone, staffName, supplierEmail, supplierAddress);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void setupListeners() {
        searchButton.addActionListener(e -> searchInvoices());
        filterButton.addActionListener(e -> filterInvoicesByDate());
        todayButton.addActionListener(e -> filterTodayInvoices());
        supplierListComboBox.addActionListener(e -> filterInvoicesBySelectedSupplier());

    }

    private void filterInvoicesBySelectedSupplier() {
        String selected = (String) supplierListComboBox.getSelectedItem();
        if (selected == null || selected.equals("Tất cả")) {
            loadAllImportReceipts();  // nếu chọn tất cả thì load toàn bộ
            return;
        }

        try {
            int supplierId = Integer.parseInt(selected.split(" - ")[0].trim());
            List<ImportReceipt> filteredImportReceipts = importReceiptDAO.getReceiptsBySupplierId(supplierId);
            loadImportReceiptToTable(filteredImportReceipts);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy ID nhân viên.");
        }
    }

    private void searchInvoices() {
        String keyword = searchTextField.getText().trim();
        if (!keyword.isEmpty()) {
            List<ImportReceipt> list = importReceiptDAO.searchImportReceiptsById(keyword);
            loadImportReceiptToTable(list);
        }
    }

    private void filterInvoicesByDate() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime start = LocalDate.parse(startDateTextField.getText().trim(), formatter).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDateTextField.getText().trim(), formatter).atTime(23, 59, 59);
            List<ImportReceipt> list = importReceiptDAO.getImportReceiptsByDateRange(start, end);
            loadImportReceiptToTable(list);
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
        List<ImportReceipt> list = importReceiptDAO.getImportReceiptsByDateRange(start, end);
        loadImportReceiptToTable(list);
    }

    private void loadAllImportReceipts() {
        List<ImportReceipt> list = importReceiptDAO.getAllReceipts();
        loadImportReceiptToTable(list);
    }

    private void loadImportReceiptToTable(List<ImportReceipt> importReceipts) {
        DefaultTableModel model = (DefaultTableModel) importReceiptTable.getModel();
        model.setRowCount(0); // clear table

        SupplierDAO supplierDAO = new SupplierDAO();
        UserDAO userDAO = new UserDAO();

        for (ImportReceipt ir : importReceipts) {
            String supplierName = supplierDAO.getSupplierNameById(ir.getSupplierId());
            String supplierPhone = supplierDAO.getSupplierPhoneById(ir.getSupplierId());
            String employee = userDAO.getUserFullName(ir.getUserId());

            model.addRow(new Object[]{
                ir.getId(),
                supplierName,
                supplierPhone,
                employee,
                ir.getCreatedAt().toLocalDate(),
                ir.getTotalAmount(),
                ir.getNote()

            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        importReceiptTable = new javax.swing.JTable();
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
        supplierListComboBox = new javax.swing.JComboBox<>();

        importReceiptTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hóa đơn", "Nhà cung cấp", "Số điện thoại", "Người nhập", "Ngày nhập", "Thành tiền", "Ghi chú"
            }
        ));
        jScrollPane1.setViewportView(importReceiptTable);

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
        jLabel4.setText("Hóa đơn nhập hàng");

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
                        .addGap(0, 9, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(supplierListComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(viewDetailButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 851, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(supplierListComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
    private javax.swing.JTable importReceiptTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JTextField startDateTextField;
    private javax.swing.JComboBox<String> supplierListComboBox;
    private javax.swing.JButton todayButton;
    private javax.swing.JButton viewDetailButton;
    // End of variables declaration//GEN-END:variables
}
