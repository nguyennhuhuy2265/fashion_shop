package view.admin;

import dao.InvoiceDAO;
import dao.InvoiceItemDAO;
import dao.ImportReceiptDAO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
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
        loadChartData();

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
        salesYearComboBox.setSelectedItem(String.valueOf(currentYear));

        // Điền danh sách năm cho chartYearComboBox
        DefaultComboBoxModel<String> chartYearModel = new DefaultComboBoxModel<>();
        for (int year = 2020; year <= currentYear; year++) {
            chartYearModel.addElement(String.valueOf(year));
        }

        chartYearComboBox.setModel(chartYearModel);
        chartYearComboBox.setSelectedItem(String.valueOf(currentYear)); 

        // Điền danh sách tháng (1-12) cho salesMonthComboBox
        DefaultComboBoxModel<String> salesMonthModel = new DefaultComboBoxModel<>();
        for (int month = 1; month <= 12; month++) {
            salesMonthModel.addElement(String.valueOf(month));
        }

        salesMonthComboBox.setModel(salesMonthModel);
        salesMonthComboBox.setSelectedItem(String.valueOf(LocalDate.now().getMonthValue()));

        // Thêm sự kiện cho ComboBox
        salesYearComboBox.addActionListener(e -> loadSalesData());
        salesMonthComboBox.addActionListener(e -> loadSalesData());
        chartYearComboBox.addActionListener(e -> loadChartData());

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

    private void loadChartData() {
        int selectedYear = Integer.parseInt((String) chartYearComboBox.getSelectedItem());

        // Lấy dữ liệu từ DB
        List<Object[]> revenueData = invoiceDAO.getRevenueByYear(selectedYear); // [month, quantity, revenue]
        List<Object[]> expenseData = importReceiptDAO.getExpensesByYear(selectedYear); // [month, expense]

        // Map dữ liệu để dễ truy xuất
        Map<Integer, Integer> revenueMap = new HashMap<>();
        for (Object[] row : revenueData) {
            int month = (Integer) row[0];
            int revenue = (Integer) row[2];
            revenueMap.put(month, revenue);
        }

        Map<Integer, Integer> expenseMap = new HashMap<>();
        for (Object[] row : expenseData) {
            int month = (Integer) row[0];
            int expense = (Integer) row[1];
            expenseMap.put(month, expense);
        }

        // Tạo dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int month = 1; month <= 12; month++) {
            String label = "T" + month; // Rút gọn label để biểu đồ gọn hơn

            int revenue = revenueMap.getOrDefault(month, 0);
            int expense = expenseMap.getOrDefault(month, 0);
            int profit = revenue - expense;

            dataset.addValue(revenue, "Doanh thu", label);
            dataset.addValue(expense, "Chi phí", label);
            dataset.addValue(profit, "Lợi nhuận", label);
        }

        // Tạo biểu đồ với thiết kế đẹp hơn
        JFreeChart chart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU NĂM " + selectedYear,
                "Tháng",
                "Số tiền (VNĐ)",
                dataset,
                PlotOrientation.VERTICAL,
                true, // legend
                true, // tooltips
                false // urls
        );

        // Tùy chỉnh giao diện biểu đồ
        customizeChart(chart);

        // Gắn chart vào giao diện
        ChartPanel cp = new ChartPanel(chart);
        cp.setPreferredSize(chartPanel.getSize());
        cp.setMouseWheelEnabled(true); // Cho phép zoom bằng chuột
        cp.setMouseZoomable(true);

        chartPanel.removeAll();
        chartPanel.setLayout(new java.awt.BorderLayout());
        chartPanel.add(cp, java.awt.BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    /**
     * Tùy chỉnh giao diện biểu đồ để trông đẹp hơn
     */
    private void customizeChart(JFreeChart chart) {
        // Thiết lập màu nền
        chart.setBackgroundPaint(new Color(245, 245, 245)); // Màu xám nhạt
        
        // Tùy chỉnh tiêu đề
        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));
        chart.getTitle().setPaint(new Color(51, 51, 51)); // Màu xám đậm
        
        // Lấy CategoryPlot để tùy chỉnh
        CategoryPlot plot = chart.getCategoryPlot();
        
        // Thiết lập màu nền của plot
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(230, 230, 230)); // Đường kẻ dọc
        plot.setRangeGridlinePaint(new Color(230, 230, 230));  // Đường kẻ ngang
        plot.setOutlineVisible(false); // Ẩn viền ngoài
        
        // Tùy chỉnh trục Y
        var yAxis = (org.jfree.chart.axis.NumberAxis) plot.getRangeAxis();
        yAxis.setNumberFormatOverride(java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN")));
        yAxis.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        yAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 10));
        yAxis.setLabelPaint(new Color(85, 85, 85));
        yAxis.setTickLabelPaint(new Color(85, 85, 85));
        
        // Tùy chỉnh trục X
        var xAxis = plot.getDomainAxis();
        xAxis.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        xAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 10));
        xAxis.setLabelPaint(new Color(85, 85, 85));
        xAxis.setTickLabelPaint(new Color(85, 85, 85));
        
        // Tùy chỉnh renderer (màu sắc của các cột)
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        
        // Thiết lập màu cho các series
        renderer.setSeriesPaint(0, new Color(52, 152, 219));  // Xanh dương - Doanh thu
        renderer.setSeriesPaint(1, new Color(231, 76, 60));   // Đỏ - Chi phí
        renderer.setSeriesPaint(2, new Color(46, 204, 113));  // Xanh lá - Lợi nhuận
        
        // Thiết lập hiệu ứng gradient (tùy chọn)
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        
        // Giảm khoảng cách giữa các cột
        renderer.setItemMargin(0.1);
        
        // Tùy chỉnh legend
        var legend = chart.getLegend();
        if (legend != null) {
            legend.setItemFont(new Font("SansSerif", Font.PLAIN, 11));
            legend.setItemPaint(new Color(85, 85, 85));
            legend.setBackgroundPaint(Color.WHITE);
            legend.setBorder(0, 0, 0, 0);
        }
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
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        chartYearComboBox = new javax.swing.JComboBox<>();
        chartPanel = new javax.swing.JPanel();

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(salesYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(salesMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Biểu đồ doanh thu");

        jLabel7.setText("Năm");

        chartYearComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout chartPanelLayout = new javax.swing.GroupLayout(chartPanel);
        chartPanel.setLayout(chartPanelLayout);
        chartPanelLayout.setHorizontalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        chartPanelLayout.setVerticalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 311, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel6)
                        .addGap(74, 74, 74)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chartYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(chartYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chartPanel;
    private javax.swing.JComboBox<String> chartYearComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> salesMonthComboBox;
    private javax.swing.JTable salesTable;
    private javax.swing.JComboBox<String> salesYearComboBox;
    // End of variables declaration//GEN-END:variables
}
