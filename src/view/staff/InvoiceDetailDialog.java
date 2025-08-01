package view.staff;

import dao.InvoiceItemDAO;
import dao.ProductDAO;
import model.InvoiceItem;
import model.Product;
import model.Invoice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.print.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class InvoiceDetailDialog extends JDialog {
    private JTable itemTable;
    private JPanel invoiceInfoPanel;
    private Invoice invoice;
    private String customerName;
    private String customerPhone;
    private String staffName;
    private final NumberFormat currencyFormat;

    public InvoiceDetailDialog(Frame parent, Invoice invoice, String customerName, String customerPhone, String staffName) {
        super(parent, "Chi tiết hóa đơn #" + invoice.getId(), true);
        this.invoice = invoice;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.staffName = staffName;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        initComponents();
        loadInvoiceDetails();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(900, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        
        // Main panel với padding
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(248, 249, 250));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Thông tin hóa đơn panel
        invoiceInfoPanel = createInvoiceInfoPanel();
        JScrollPane infoScroll = new JScrollPane(invoiceInfoPanel);
        infoScroll.setPreferredSize(new Dimension(0, 200));
        infoScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            "Thông tin hóa đơn", 
            0, 0, new Font(Font.SANS_SERIF, Font.BOLD, 14)
        ));
        mainPanel.add(infoScroll, BorderLayout.CENTER);
        
        // Bảng sản phẩm
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        
        JLabel titleLabel = new JLabel("CHI TIẾT HÓA ĐƠN", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 58, 64));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createInvoiceInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(248, 249, 250));
        
        // Bảng sản phẩm
        String[] columns = {"Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        itemTable = new JTable(model);
        styleTable();
        
        JScrollPane scrollPane = new JScrollPane(itemTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            "Danh sách sản phẩm", 
            0, 0, new Font(Font.SANS_SERIF, Font.BOLD, 14)
        ));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void styleTable() {
        itemTable.setRowHeight(30);
        itemTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        itemTable.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        itemTable.getTableHeader().setBackground(new Color(233, 236, 239));
        itemTable.getTableHeader().setForeground(new Color(52, 58, 64));
        itemTable.setSelectionBackground(new Color(220, 248, 255));
        itemTable.setGridColor(new Color(224, 224, 224));
        itemTable.setShowVerticalLines(true);
        itemTable.setShowHorizontalLines(true);
        
        // Căn chỉnh cột
        itemTable.getColumnModel().getColumn(0).setPreferredWidth(80);   // Mã SP
        itemTable.getColumnModel().getColumn(1).setPreferredWidth(300);  // Tên SP
        itemTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Số lượng
        itemTable.getColumnModel().getColumn(3).setPreferredWidth(120);  // Đơn giá
        itemTable.getColumnModel().getColumn(4).setPreferredWidth(120);  // Thành tiền
        
        // Căn giữa cho các cột số
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        itemTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        itemTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        itemTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        itemTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(new Color(248, 249, 250));
        
        JButton printButton = createStyledButton("In hóa đơn", new Color(108, 117, 125));
        JButton closeButton = createStyledButton("Đóng", new Color(108, 117, 125));
        
        printButton.addActionListener(e -> printInvoice());
        closeButton.addActionListener(e -> dispose());
        
        panel.add(printButton);
        panel.add(closeButton);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(140, 40));
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = bgColor;
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
        
        return button;
    }

    private void loadInvoiceDetails() {
        try {
            InvoiceItemDAO itemDAO = new InvoiceItemDAO();
            ProductDAO productDAO = new ProductDAO();
            List<InvoiceItem> items = itemDAO.getItemsByInvoiceId(invoice.getId());
            
            // Load thông tin hóa đơn
            loadInvoiceInfo(items.size());
            
            // Load bảng sản phẩm
            DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
            model.setRowCount(0);
            
            for (InvoiceItem item : items) {
                Product product = productDAO.getProductById(item.getProductId());
                String productName = (product != null) ? product.getName() : "Sản phẩm không tồn tại";
                
                model.addRow(new Object[]{
                    item.getProductId(),
                    productName,
                    item.getQuantity(),
                    currencyFormat.format(item.getUnitPrice()),
                    currencyFormat.format(item.getTotalPrice())
                });
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải chi tiết hóa đơn: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadInvoiceInfo(int itemCount) {
        invoiceInfoPanel.removeAll();
        
        // Tạo layout 2 cột
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        // Thông tin bên trái
        leftPanel.add(createInfoRow("Mã hóa đơn:", "#" + invoice.getId()));
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(createInfoRow("Ngày tạo:", invoice.getCreatedAt().toString()));
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(createInfoRow("Khách hàng:", customerName));
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(createInfoRow("Số điện thoại:", customerPhone));
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(createInfoRow("Nhân viên:", staffName));
        
        // Thông tin bên phải
        rightPanel.add(createInfoRow("Số mặt hàng:", String.valueOf(itemCount)));
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(createInfoRow("Tổng tiền:", currencyFormat.format(invoice.getTotalAmount())));
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(createInfoRow("Khách đưa:", currencyFormat.format(invoice.getPaidAmount())));
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(createInfoRow("Tiền thối:", currencyFormat.format(invoice.getChangeAmount())));
        rightPanel.add(Box.createVerticalStrut(8));
        
        // Ghi chú (full width)
        if (invoice.getNote() != null && !invoice.getNote().trim().isEmpty()) {
            rightPanel.add(createInfoRow("Ghi chú:", invoice.getNote()));
        }
        
        // Layout 2 cột
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);
        
        invoiceInfoPanel.add(contentPanel);
        invoiceInfoPanel.revalidate();
        invoiceInfoPanel.repaint();
    }
    
    private JPanel createInfoRow(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        labelComp.setForeground(new Color(73, 80, 87));
        labelComp.setPreferredSize(new Dimension(120, 25));
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        valueComp.setForeground(new Color(33, 37, 41));
        
        panel.add(labelComp, BorderLayout.WEST);
        panel.add(valueComp, BorderLayout.CENTER);
        
        return panel;
    }

    private void printInvoice() {
        try {
            // Tạo nội dung in đầy đủ
            JPanel printPanel = createPrintableInvoice();
            
            // Tạo frame tạm để render panel
            JFrame tempFrame = new JFrame();
            tempFrame.add(printPanel);
            tempFrame.pack();
            tempFrame.setVisible(false); // Ẩn frame
            
            // In trực tiếp không hiện dialog
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(new Printable() {
                public int print(Graphics g, PageFormat pf, int pageIndex) {
                    if (pageIndex > 0) return NO_SUCH_PAGE;
                    
                    Graphics2D g2d = (Graphics2D) g;
                    
                    // Set màu đen cho text
                    g2d.setColor(Color.BLACK);
                    
                    // Translate to printable area
                    g2d.translate(pf.getImageableX(), pf.getImageableY());
                    
                    // Scale để fit trang
                    double scaleX = pf.getImageableWidth() / printPanel.getWidth();
                    double scaleY = pf.getImageableHeight() / printPanel.getHeight();
                    double scale = Math.min(scaleX, scaleY);
                    g2d.scale(scale, scale);
                    
                    // Enable anti-aliasing cho text đẹp hơn
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                       RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                       RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Ensure components are properly rendered
                    printPanel.setDoubleBuffered(false);
                    printPanel.paint(g2d);
                    printPanel.setDoubleBuffered(true);
                    
                    return PAGE_EXISTS;
                }
            });
            
            // Set page format to A4
            PageFormat pf = job.defaultPage();
            pf.setOrientation(PageFormat.PORTRAIT);
            
            // In mà không hiện dialog
            job.print();
            
            // Cleanup
            tempFrame.dispose();
            
            JOptionPane.showMessageDialog(this, 
                "In hóa đơn thành công!", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Không thể in hóa đơn: " + e.getMessage(),
                "Lỗi in ấn", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel createPrintableInvoice() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Header hóa đơn
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel companyLabel = new JLabel("CỬA HÀNG THỜI TRANG HANA MART", JLabel.CENTER);
        companyLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        companyLabel.setForeground(Color.BLACK);
        companyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel addressLabel = new JLabel("Địa chỉ: 219 Đường Phạm Như Xương, Điện Ngọc, TP.Đà Nẵng", JLabel.CENTER);
        addressLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        addressLabel.setForeground(Color.BLACK);
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel phoneLabel = new JLabel("ĐT: 0123456789 | Email: hana@mart.com", JLabel.CENTER);
        phoneLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        phoneLabel.setForeground(Color.BLACK);
        phoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("HÓA ĐƠN BÁN HÀNG", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        headerPanel.add(companyLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(addressLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(phoneLabel);
        headerPanel.add(titleLabel);
        
        // Thông tin hóa đơn
        JPanel infoPanel = new JPanel(new GridLayout(6, 2, 15, 8));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        addPrintInfoRow(infoPanel, "Mã hóa đơn:", "#" + invoice.getId());
        addPrintInfoRow(infoPanel, "Ngày:", invoice.getCreatedAt().toString());
        addPrintInfoRow(infoPanel, "Khách hàng:", customerName);
        addPrintInfoRow(infoPanel, "SĐT:", customerPhone);
        addPrintInfoRow(infoPanel, "Nhân viên:", staffName);
        addPrintInfoRow(infoPanel, "Ghi chú:", invoice.getNote() != null ? invoice.getNote() : "");
        
        // Bảng sản phẩm
        DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
        String[][] data = new String[model.getRowCount()][model.getColumnCount()];
        String[] columnNames = {"Mã SP", "Tên sản phẩm", "SL", "Đơn giá", "Thành tiền"};
        
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                data[i][j] = model.getValueAt(i, j).toString();
            }
        }
        
        JTable printTable = new JTable(data, columnNames);
        printTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        printTable.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        printTable.getTableHeader().setForeground(Color.BLACK);
        printTable.setForeground(Color.BLACK); // Đảm bảo text màu đen
        printTable.setRowHeight(25);
        printTable.setGridColor(Color.BLACK);
        printTable.setShowGrid(true);
        printTable.setBackground(Color.WHITE);
        printTable.getTableHeader().setBackground(Color.WHITE);
        
        // Tổng tiền
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JPanel totalInfo = new JPanel(new GridLayout(3, 2, 10, 5));
        totalInfo.setBackground(Color.WHITE);
        addPrintInfoRow(totalInfo, "Tổng tiền:", currencyFormat.format(invoice.getTotalAmount()));
//        addPrintInfoRow(totalInfo, "Khách đưa:", currencyFormat.format(invoice.getPaidAmount()));
//        addPrintInfoRow(totalInfo, "Tiền thối:", currencyFormat.format(invoice.getChangeAmount()));
        
        totalPanel.add(totalInfo);
        
        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        JLabel thankLabel = new JLabel("Cảm ơn quý khách đã mua hàng!", JLabel.CENTER);
        thankLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        thankLabel.setForeground(Color.BLACK);
        
        JPanel signPanel = new JPanel(new GridLayout(1, 2));
        signPanel.setBackground(Color.WHITE);
        signPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        
        footerPanel.add(thankLabel, BorderLayout.NORTH);
        footerPanel.add(signPanel, BorderLayout.SOUTH);
        
        // Ghép tất cả
        panel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(infoPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(printTable), BorderLayout.CENTER);
        centerPanel.add(totalPanel, BorderLayout.SOUTH);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(footerPanel, BorderLayout.SOUTH);
        
        panel.setPreferredSize(new Dimension(600, 800));
        return panel;
    }
    
    private void addPrintInfoRow(JPanel parent, String label, String value) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        labelComp.setForeground(Color.BLACK); // Đảm bảo màu đen
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        valueComp.setForeground(Color.BLACK); // Đảm bảo màu đen
        
        parent.add(labelComp);
        parent.add(valueComp);
    }
}