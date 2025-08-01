package view.staff;

import dao.CustomerDAO;
import javax.swing.*;
import java.awt.*;
import model.Customer;

public class EditCustomerDialog extends JDialog {
    
    // Components
    private JTextField nameField, phoneField;
    private JButton saveButton, cancelButton;
    
    // Data
    private Customer customer; // null nếu là thêm mới
    private boolean saved = false;
    
    public EditCustomerDialog(Frame owner, Customer customer) {
        super(owner, true);
        this.customer = customer;
        
        setTitle(customer == null ? "Thêm khách hàng" : "Chỉnh sửa khách hàng");
        initComponents();
        
        setSize(350, 180);
        setResizable(false);
        
        if (customer != null) {
            populateFields(customer);
        }
    }
    
    private void initComponents() {
        // Create labels
        JLabel nameLabel = new JLabel("Họ tên:");
        JLabel phoneLabel = new JLabel("SĐT:");
        
        // Create input fields
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        
        // Create buttons
        saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");
        
        // Create input panel with better spacing
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(phoneLabel);
        inputPanel.add(phoneField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Layout main panel
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Add event listeners
        saveButton.addActionListener(e -> onSave());
        cancelButton.addActionListener(e -> onCancel());
        
        // Set default button and focus
        getRootPane().setDefaultButton(saveButton);
        nameField.requestFocusInWindow();
        
        pack();
        setLocationRelativeTo(getOwner());
    }
    
    private void populateFields(Customer customer) {
        nameField.setText(customer.getName());
        phoneField.setText(customer.getPhone());
    }
    
    private void onSave() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        
        // Validate input
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập đầy đủ thông tin.", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        CustomerDAO dao = new CustomerDAO();
        
        try {
            if (customer == null) {
                // Thêm mới khách hàng
                Customer newCustomer = new Customer();
                newCustomer.setName(name);
                newCustomer.setPhone(phone);
                newCustomer.setPoints(0);
                
                if (dao.insertCustomer(newCustomer)) {
                    saved = true;
                    JOptionPane.showMessageDialog(this, 
                        "Thêm khách hàng thành công!", 
                        "Thông báo", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Thêm khách hàng thất bại.", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Chỉnh sửa khách hàng
                customer.setName(name);
                customer.setPhone(phone);
                
                if (dao.updateCustomer(customer)) {
                    saved = true;
                    JOptionPane.showMessageDialog(this, 
                        "Cập nhật khách hàng thành công!", 
                        "Thông báo", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Cập nhật khách hàng thất bại.", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Có lỗi xảy ra: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void onCancel() {
        dispose();
    }
    
    public boolean isSaved() {
        return saved;
    }
}