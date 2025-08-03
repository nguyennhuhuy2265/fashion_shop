package view.admin;

import dao.InvoiceDAO;
import dao.InvoiceItemDAO;
import dao.ImportReceiptDAO;
import java.awt.Frame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class RevenueForm extends javax.swing.JPanel {

    private InvoiceItemDAO invoiceItemDAO;
    private InvoiceDAO invoiceDAO;
    private ImportReceiptDAO importReceiptDAO;

    public RevenueForm() {
        // Khởi tạo các DAO
        invoiceItemDAO = new InvoiceItemDAO();
        invoiceDAO = new InvoiceDAO();
        importReceiptDAO = new ImportReceiptDAO();
        
        initComponents();
        initializeComboBoxes();
        loadSalesData(); // Tải dữ liệu ban đầu cho salesTable
        loadRevenueData(); // Tải dữ liệu ban đầu cho revenueTable
    }

    // Khởi tạo danh sách năm và tháng cho ComboBox
    private void initializeComboBoxes() {
        // Lấy năm hiện tại
        int currentYear = LocalDate.now().getYear();

        // Điền danh sách năm cho salesYearComboBox
        DefaultComboBoxModel<String> salesYearModel = new DefaultComboBoxModel<>();
        for (int year = 2020; year <= currentYear; year++) {
            salesYearModel.addElement(String.valueOf(year));
        }
        salesYearComboBox.setModel(salesYearModel);

        // Điền danh sách năm cho revenueYearComboBox
        DefaultComboBoxModel<String> revenueYearModel = new DefaultComboBoxModel<>();
        for (int year = 2020; year <= currentYear; year++) {
            revenueYearModel.addElement(String.valueOf(year));
        }
        revenueYearComboBox.setModel(revenueYearModel);

        // Điền danh sách năm cho chartYearComboBox
        DefaultComboBoxModel<String> chartYearModel = new DefaultComboBoxModel<>();
        for (int year = 2020; year <= currentYear; year++) {
            chartYearModel.addElement(String.valueOf(year));
        }
        chartYearComboBox.setModel(chartYearModel);

        // Điền danh sách tháng (1-12) cho salesMonthComboBox
        DefaultComboBoxModel<String> salesMonthModel = new DefaultComboBoxModel<>();
        for (int month = 1; month <= 12; month++) {
            salesMonthModel.addElement(String.valueOf(month));
        }
        salesMonthComboBox.setModel(salesMonthModel);

        // Điền danh sách tháng (Tất cả, 1-12) cho chartMonthComboBox
        DefaultComboBoxModel<String> chartMonthModel = new DefaultComboBoxModel<>();
        chartMonthModel.addElement("Tất cả");
        for (int month = 1; month <= 12; month++) {
            chartMonthModel.addElement(String.valueOf(month));
        }
        chartMonthComboBox.setModel(chartMonthModel);

        // Thêm sự kiện cho ComboBox
        salesYearComboBox.addActionListener(e -> loadSalesData());
        salesMonthComboBox.addActionListener(e -> loadSalesData());
        revenueYearComboBox.addActionListener(e -> loadRevenueData());

        // Thêm sự kiện cho nút biểu đồ cột
        columnChartButton.addActionListener(e -> showChartDialog(true));
    }

    // Tải dữ liệu cho salesTable
    private void loadSalesData() {
        int selectedYear = Integer.parseInt((String) salesYearComboBox.getSelectedItem());
        int selectedMonth = Integer.parseInt((String) salesMonthComboBox.getSelectedItem());
        List<Object[]> salesData = invoiceItemDAO.getSalesByYearAndMonth(selectedYear, selectedMonth);
        
        DefaultTableModel salesModel = (DefaultTableModel) salesTable.getModel();
        salesModel.setRowCount(0); // Xóa dữ liệu cũ
        for (Object[] row : salesData) {
            salesModel.addRow(row); // Thêm dữ liệu mới: [product_id, product_name, total_quantity]
        }
    }

    // Tải dữ liệu cho revenueTable
    private void loadRevenueData() {
        int selectedYear = Integer.parseInt((String) revenueYearComboBox.getSelectedItem());
        List<Object[]> revenueData = invoiceDAO.getRevenueByYear(selectedYear);
        List<Object[]> expenseData = importReceiptDAO.getExpensesByYear(selectedYear);
        
        // Tạo map cho chi phí để dễ tra cứu
        Map<Integer, Double> expensesMap = new HashMap<>();
        for (Object[] expense : expenseData) {
            expensesMap.put((Integer) expense[0], (Double) expense[1]);
        }
        
        DefaultTableModel revenueModel = (DefaultTableModel) revenueTable.getModel();
        revenueModel.setRowCount(0); // Xóa dữ liệu cũ
        
        // Kết hợp dữ liệu doanh thu và chi phí
        for (Object[] revenue : revenueData) {
            int month = (Integer) revenue[0];
            int quantity = (Integer) revenue[1];
            double totalRevenue = (Double) revenue[2];
            double totalExpense = expensesMap.getOrDefault(month, 0.0);
            double profit = totalRevenue - totalExpense;
            revenueModel.addRow(new Object[]{month, quantity, totalRevenue, totalExpense, profit});
        }
    }

    // Hiển thị biểu đồ trong JDialog
    private void showChartDialog(boolean isBarChart) {
        int selectedYear = Integer.parseInt((String) chartYearComboBox.getSelectedItem());
        String selectedMonth = (String) chartMonthComboBox.getSelectedItem();
        
        // Lấy dữ liệu
        List<Object[]> revenueData = invoiceDAO.getRevenueByYear(selectedYear);
        List<Object[]> expenseData = importReceiptDAO.getExpensesByYear(selectedYear);
        
        // Tạo map cho chi phí
        Map<Integer, Double> expensesMap = new HashMap<>();
        for (Object[] expense : expenseData) {
            expensesMap.put((Integer) expense[0], (Double) expense[1]);
        }
        
        // Tạo dataset cho biểu đồ
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Object[] revenue : revenueData) {
            int month = (Integer) revenue[0];
            double totalRevenue = (Double) revenue[2];
            double totalExpense = expensesMap.getOrDefault(month, 0.0);
            double profit = totalRevenue - totalExpense;
            
            // Nếu chọn "Tất cả" hoặc tháng cụ thể
            if ("Tất cả".equals(selectedMonth) || String.valueOf(month).equals(selectedMonth)) {
                dataset.addValue(totalRevenue, "Tổng thu", "Tháng " + month);
                dataset.addValue(totalExpense, "Tổng chi", "Tháng " + month);
                dataset.addValue(profit, "Lợi nhuận", "Tháng " + month);
            }
        }
        
        // Kiểm tra dữ liệu trống
        if (dataset.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để hiển thị biểu đồ cho năm " + selectedYear + ("Tất cả".equals(selectedMonth) ? "" : " tháng " + selectedMonth));
            return;
        }
        
        // Tạo biểu đồ
        String chartTitle = "Biểu đồ doanh thu năm " + selectedYear;
        if (!"Tất cả".equals(selectedMonth)) {
            chartTitle += " tháng " + selectedMonth;
        }
        JFreeChart chart = ChartFactory.createBarChart(
            chartTitle, // Tiêu đề
            "Tháng", // Trục X
            "Số tiền", // Trục Y
            dataset, // Dữ liệu
            PlotOrientation.VERTICAL,
            true, // Hiển thị legend
            true, // Hiển thị tooltips
            false // Không hiển thị URLs
        );
        
        // Tạo JDialog để hiển thị biểu đồ
        JDialog chartDialog = new JDialog((Frame) null, chartTitle, true);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        chartDialog.add(chartPanel);
        chartDialog.pack();
        chartDialog.setLocationRelativeTo(this);
        chartDialog.setVisible(true);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        salesYearComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        salesMonthComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        salesTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        revenueYearComboBox = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        revenueTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        chartYearComboBox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        chartMonthComboBox = new javax.swing.JComboBox<>();
        columnChartButton = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Thống kê doanh số");

        salesYearComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Năm");

        jLabel5.setText("Tháng");

        salesMonthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        salesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã sản phẩm", "Tên sản Phẩm", "Số lượng bán"
            }
        ));
        jScrollPane1.setViewportView(salesTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(8, 8, 8)
                        .addComponent(salesYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salesMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(salesYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(salesMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Thống kê doanh thu");

        jLabel4.setText("Năm");

        revenueYearComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        revenueTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tháng", "Số lượng sản phẩm", "Tổng thu", "Tổng chi", "Lợi nhuân"
            }
        ));
        jScrollPane2.setViewportView(revenueTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addGap(7, 7, 7)
                        .addComponent(revenueYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(revenueYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Biểu đồ doanh thu");

        jLabel7.setText("Năm");

        chartYearComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setText("Tháng");

        chartMonthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        columnChartButton.setText("Biểu đồ cột");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(206, 206, 206))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(159, 159, 159)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chartYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chartMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(218, 218, 218)
                        .addComponent(columnChartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(172, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel6)
                .addGap(38, 38, 38)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(chartYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(chartMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(columnChartButton)
                .addContainerGap(129, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> chartMonthComboBox;
    private javax.swing.JComboBox<String> chartYearComboBox;
    private javax.swing.JButton columnChartButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable revenueTable;
    private javax.swing.JComboBox<String> revenueYearComboBox;
    private javax.swing.JComboBox<String> salesMonthComboBox;
    private javax.swing.JTable salesTable;
    private javax.swing.JComboBox<String> salesYearComboBox;
    // End of variables declaration//GEN-END:variables
}
