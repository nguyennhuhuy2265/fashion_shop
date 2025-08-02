package view.admin;

import java.awt.CardLayout;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import view.LoginForm;

public class AdminMainForm extends javax.swing.JFrame {

    public AdminMainForm() {
        initComponents();
        setSize(1300, 700);

        // Tạo menu popup
        javax.swing.JPopupMenu invoiceMenu = new javax.swing.JPopupMenu();
        javax.swing.JMenuItem viewInvoice = new javax.swing.JMenuItem("Hóa đơn bán hàng");
        javax.swing.JMenuItem viewImportReceipt = new javax.swing.JMenuItem("Phiếu nhập hàng");

        invoiceMenu.add(viewInvoice);
        invoiceMenu.add(viewImportReceipt);

        // Thêm các panel vào contentPanelAdmin
        contentPanelAdmin.add(new ProductForm(), "product");
        contentPanelAdmin.add(new InvoiceForm(), "invoice");
        contentPanelAdmin.add(new ImportReceipt(), "import_receipt");
        contentPanelAdmin.add(new CustomerForm(), "customer");
        contentPanelAdmin.add(new ImportForm(), "import");
        contentPanelAdmin.add(new RevenueForm(), "revenue");
        contentPanelAdmin.add(new StaffForm(), "staff");

        CardLayout layout = (CardLayout) contentPanelAdmin.getLayout();

        // Gắn sự kiện cho các JMenuItem
        viewInvoice.addActionListener(e -> layout.show(contentPanelAdmin, "invoice"));
        viewImportReceipt.addActionListener(e -> layout.show(contentPanelAdmin, "import_receipt"));

        // Gắn sự kiện duy nhất cho nút invoiceButton để hiện menu
        invoiceButton.addActionListener(e -> {
            invoiceMenu.show(invoiceButton, 0, invoiceButton.getHeight());
        });

        // Các nút khác
        productButton.addActionListener(e -> layout.show(contentPanelAdmin, "product"));
        customerButton.addActionListener(e -> layout.show(contentPanelAdmin, "customer"));
        importButton.addActionListener(e -> layout.show(contentPanelAdmin, "import"));
        revenueButton.addActionListener(e -> layout.show(contentPanelAdmin, "revenue"));
        staffButton.addActionListener(e -> layout.show(contentPanelAdmin, "staff"));

        logOutButton.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuPanelaAdmin = new javax.swing.JPanel();
        productButton = new javax.swing.JButton();
        invoiceButton = new javax.swing.JButton();
        revenueButton = new javax.swing.JButton();
        staffButton = new javax.swing.JButton();
        logOutButton = new javax.swing.JButton();
        importButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        customerButton = new javax.swing.JButton();
        contentPanelAdmin = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        menuPanelaAdmin.setBackground(new java.awt.Color(153, 153, 255));

        productButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\fashion.png")); // NOI18N
        productButton.setText("Sản phẩm");

        invoiceButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\invoice.png")); // NOI18N
        invoiceButton.setText("Hoá đơn");

        revenueButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\revenue.png")); // NOI18N
        revenueButton.setText("Doanh thu");

        staffButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\staff.png")); // NOI18N
        staffButton.setText("Nhân viên");

        logOutButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\logout.png")); // NOI18N
        logOutButton.setText("Đăng xuất");

        importButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\bill.png")); // NOI18N
        importButton.setText("Nhập hàng");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\logo.png")); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("FASHION");

        customerButton.setIcon(new javax.swing.ImageIcon("C:\\Code\\Java\\fashion_shop\\src\\assets\\customer.png")); // NOI18N
        customerButton.setText("Khách hàng");

        javax.swing.GroupLayout menuPanelaAdminLayout = new javax.swing.GroupLayout(menuPanelaAdmin);
        menuPanelaAdmin.setLayout(menuPanelaAdminLayout);
        menuPanelaAdminLayout.setHorizontalGroup(
            menuPanelaAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuPanelaAdminLayout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(menuPanelaAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(productButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(invoiceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(revenueButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(staffButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logOutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(importButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(customerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(37, 37, 37))
        );
        menuPanelaAdminLayout.setVerticalGroup(
            menuPanelaAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelaAdminLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(productButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(invoiceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(revenueButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(staffButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(customerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(importButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                .addComponent(logOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        contentPanelAdmin.setBackground(new java.awt.Color(51, 51, 255));
        contentPanelAdmin.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(menuPanelaAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentPanelAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuPanelaAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contentPanelAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminMainForm().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanelAdmin;
    private javax.swing.JButton customerButton;
    private javax.swing.JButton importButton;
    private javax.swing.JButton invoiceButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton logOutButton;
    private javax.swing.JPanel menuPanelaAdmin;
    private javax.swing.JButton productButton;
    private javax.swing.JButton revenueButton;
    private javax.swing.JButton staffButton;
    // End of variables declaration//GEN-END:variables
}
