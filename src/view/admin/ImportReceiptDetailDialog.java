package view.admin;

import dao.ImportItemDAO;
import dao.ProductDAO;
import model.ImportItem;
import model.ImportReceipt;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.print.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ImportReceiptDetailDialog extends JDialog {

    private JTable itemTable;
    private JPanel receiptInfoPanel;
    private ImportReceipt receipt;
    private String supplierName;
    private String supplierPhone;
    private String supplierEmail;
    private String supplierAddress;
    private String staffName;
    private final NumberFormat currencyFormat;
    private final DateTimeFormatter dateFormatter;

    public ImportReceiptDetailDialog(Frame parent, ImportReceipt receipt, String supplierName, String supplierPhone, String staffName, String supplierEmail, String supplierAddress) {
        super(parent, "Chi tiết phiếu nhập #" + receipt.getId(), true);
        this.receipt = receipt;
        this.supplierName = supplierName;
        this.supplierPhone = supplierPhone;
        this.supplierEmail = supplierEmail;
        this.supplierAddress = supplierAddress;
        this.staffName = staffName;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        initComponents();
        loadReceiptDetails();
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

        // Thông tin phiếu nhập panel
        receiptInfoPanel = createReceiptInfoPanel();
        JScrollPane infoScroll = new JScrollPane(receiptInfoPanel);
        infoScroll.setPreferredSize(new Dimension(0, 250)); // Tăng chiều cao để chứa ghi chú
        infoScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Thông tin phiếu nhập",
                0, 0, new Font("Arial", Font.BOLD, 14)
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

        JLabel titleLabel = new JLabel("PHIẾU NHẬP HÀNG", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 58, 64));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReceiptInfoPanel() {
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
                0, 0, new Font("Arial", Font.BOLD, 14)
        ));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void styleTable() {
        itemTable.setRowHeight(30);
        itemTable.setFont(new Font("Arial", Font.PLAIN, 12));
        itemTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
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

        JButton printButton = createStyledButton("In phiếu nhập", new Color(108, 117, 125));
        JButton closeButton = createStyledButton("Đóng", new Color(108, 117, 125));

        printButton.addActionListener(e -> printReceipt());
        closeButton.addActionListener(e -> dispose());

        panel.add(printButton);
        panel.add(closeButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(140, 40));
        button.setFont(new Font("Arial", Font.BOLD, 12));
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

    private void loadReceiptDetails() {
        try {
            ImportItemDAO itemDAO = new ImportItemDAO();
            ProductDAO productDAO = new ProductDAO();
            List<ImportItem> items = itemDAO.getItemsByImportReceiptId(receipt.getId());

            // Load thông tin phiếu nhập
            loadReceiptInfo(items.size());

            // Load bảng sản phẩm
            DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
            model.setRowCount(0);

            for (ImportItem item : items) {
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
                    "Lỗi khi tải chi tiết phiếu nhập: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadReceiptInfo(int itemCount) {
        receiptInfoPanel.removeAll();

        // Tạo layout 2 cột
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);

        // Thông tin bên trái
        leftPanel.add(createInfoRow("Mã phiếu nhập:", "#" + receipt.getId()));
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(createInfoRow("Ngày nhập:", dateFormatter.format(receipt.getCreatedAt())));
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(createInfoRow("Nhà cung cấp:", supplierName));
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(createInfoRow("Địa chỉ:", supplierAddress != null ? supplierAddress : ""));

        // Thông tin bên phải
        rightPanel.add(createInfoRow("Số điện thoại:", supplierPhone));
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(createInfoRow("Email:", supplierEmail != null ? supplierEmail : ""));
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(createInfoRow("Nhân viên nhập:", staffName));
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(createInfoRow("Số mặt hàng:", String.valueOf(itemCount)));
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(createInfoRow("Tổng tiền:", currencyFormat.format(receipt.getTotalAmount())));

        // Ghi chú (nếu có)
        if (receipt.getNote() != null && !receipt.getNote().trim().isEmpty()) {
            JPanel notePanel = new JPanel(new BorderLayout());
            notePanel.setBackground(Color.WHITE);
            notePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            notePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            JLabel noteLabel = new JLabel("Ghi chú:");
            noteLabel.setFont(new Font("Arial", Font.BOLD, 12));
            noteLabel.setForeground(new Color(73, 80, 87));
            noteLabel.setPreferredSize(new Dimension(120, 25));

            JTextArea noteValue = new JTextArea(receipt.getNote());
            noteValue.setFont(new Font("Arial", Font.PLAIN, 12));
            noteValue.setForeground(new Color(33, 37, 41));
            noteValue.setEditable(false);
            noteValue.setLineWrap(true);
            noteValue.setWrapStyleWord(true);
            noteValue.setBackground(Color.WHITE);

            notePanel.add(noteLabel, BorderLayout.WEST);
            notePanel.add(noteValue, BorderLayout.CENTER);

            rightPanel.add(Box.createVerticalStrut(8));
            rightPanel.add(notePanel);
        }

        // Layout 2 cột
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);

        receiptInfoPanel.add(contentPanel);
        receiptInfoPanel.revalidate();
        receiptInfoPanel.repaint();
    }

    private JPanel createInfoRow(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.BOLD, 12));
        labelComp.setForeground(new Color(73, 80, 87));
        labelComp.setPreferredSize(new Dimension(120, 25));

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Arial", Font.PLAIN, 12));
        valueComp.setForeground(new Color(33, 37, 41));

        panel.add(labelComp, BorderLayout.WEST);
        panel.add(valueComp, BorderLayout.CENTER);

        return panel;
    }

    private void printReceipt() {
        try {
            // Tạo nội dung in đầy đủ
            JPanel printPanel = createPrintableReceipt();

            // Tạo frame tạm để render panel
            JFrame tempFrame = new JFrame();
            tempFrame.add(printPanel);
            tempFrame.pack();
            tempFrame.setVisible(false); // Ẩn frame

            // In trực tiếp không hiện dialog
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(new Printable() {
                public int print(Graphics g, PageFormat pf, int pageIndex) {
                    if (pageIndex > 0) {
                        return NO_SUCH_PAGE;
                    }

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
                    "In phiếu nhập thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Không thể in phiếu nhập: " + e.getMessage(),
                    "Lỗi in ấn", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createPrintableReceipt() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header phiếu nhập
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);

        JLabel companyLabel = new JLabel("CỬA HÀNG THỜI TRANG HANA MART", JLabel.CENTER);
        companyLabel.setFont(new Font("Arial", Font.BOLD, 18));
        companyLabel.setForeground(Color.BLACK);
        companyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel addressLabel = new JLabel("Địa chỉ: 219 Đường Phạm Như Xương, Điện Ngọc, TP. Đà Nẵng", JLabel.CENTER);
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        addressLabel.setForeground(Color.BLACK);
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel contactLabel = new JLabel("ĐT: 0123 456 789 | Email: hana@mart.com", JLabel.CENTER);
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        contactLabel.setForeground(Color.BLACK);
        contactLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("PHIẾU NHẬP HÀNG", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        headerPanel.add(companyLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(addressLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(contactLabel);
        headerPanel.add(titleLabel);

        // Thông tin phiếu nhập
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 15, 8));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        addPrintInfoRow(infoPanel, "Mã phiếu nhập:", "#" + receipt.getId());
        addPrintInfoRow(infoPanel, "Ngày nhập:", dateFormatter.format(receipt.getCreatedAt()));
        addPrintInfoRow(infoPanel, "Nhà cung cấp:", supplierName);
        addPrintInfoRow(infoPanel, "Địa chỉ:", supplierAddress != null ? supplierAddress : "");
        addPrintInfoRow(infoPanel, "SĐT:", supplierPhone);
        addPrintInfoRow(infoPanel, "Email:", supplierEmail != null ? supplierEmail : "");
        addPrintInfoRow(infoPanel, "Nhân viên nhập:", staffName);
        if (receipt.getNote() != null && !receipt.getNote().trim().isEmpty()) {
            addPrintInfoRow(infoPanel, "Ghi chú:", receipt.getNote());
        }

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
        printTable.setFont(new Font("Arial", Font.PLAIN, 11));
        printTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        printTable.getTableHeader().setForeground(Color.BLACK);
        printTable.setForeground(Color.BLACK);
        printTable.setRowHeight(25);
        printTable.setGridColor(Color.BLACK);
        printTable.setShowGrid(true);
        printTable.setBackground(Color.WHITE);
        printTable.getTableHeader().setBackground(Color.WHITE);

        // Căn chỉnh cột cho bảng in
        printTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        printTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        printTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        printTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        printTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        printTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        printTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        printTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        printTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        // Tổng tiền
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JPanel totalInfo = new JPanel(new GridLayout(1, 2, 10, 5));
        totalInfo.setBackground(Color.WHITE);
        addPrintInfoRow(totalInfo, "Tổng tiền:", currencyFormat.format(receipt.getTotalAmount()));

        totalPanel.add(totalInfo);

        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JLabel thankLabel = new JLabel("Cảm ơn nhà cung cấp!", JLabel.CENTER);
        thankLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        thankLabel.setForeground(Color.BLACK);

        JPanel signPanel = new JPanel(new GridLayout(1, 2, 100, 0));
        signPanel.setBackground(Color.WHITE);
        signPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        JLabel supplierSignLabel = new JLabel("Người giao hàng", JLabel.CENTER);
        supplierSignLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        supplierSignLabel.setForeground(Color.BLACK);

        JLabel receiverSignLabel = new JLabel("Người nhận hàng", JLabel.CENTER);
        receiverSignLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        receiverSignLabel.setForeground(Color.BLACK);

        signPanel.add(supplierSignLabel);
        signPanel.add(receiverSignLabel);

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
        labelComp.setFont(new Font("Arial", Font.BOLD, 11));
        labelComp.setForeground(Color.BLACK);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Arial", Font.PLAIN, 11));
        valueComp.setForeground(Color.BLACK);

        parent.add(labelComp);
        parent.add(valueComp);
    }
}