package view.admin;

import dao.SupplierDAO;
import model.Supplier;

import javax.swing.*;
import java.awt.*;

public class EditSupplierDialog extends JDialog {

    private JTextField nameField, phoneField, emailField, addressField;
    private JButton saveButton, cancelButton;
    private boolean saved = false;
    private Supplier supplier; // null nếu là thêm mới

    public EditSupplierDialog(Frame parent, Supplier supplier) {
        super(parent, supplier == null ? "Thêm nhà cung cấp" : "Chỉnh sửa nhà cung cấp", true);
        this.supplier = supplier;
        initComponents();
        if (supplier != null) {
            loadSupplierData();
        }
    }

    private void initComponents() {
        setSize(400, 240);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Tên nhà cung cấp:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Số điện thoại:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Địa chỉ:"));
        addressField = new JTextField();
        formPanel.add(addressField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> onSave());
        cancelButton.addActionListener(e -> dispose());
    }

    private void loadSupplierData() {
        nameField.setText(supplier.getName());
        phoneField.setText(supplier.getPhone());
        emailField.setText(supplier.getEmail());
        addressField.setText(supplier.getAddress());
    }

    private void onSave() {
        if (!validateForm()) {
            return; // Nếu không hợp lệ thì dừng lại
        }
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên và số điện thoại không được để trống.");
            return;
        }

        SupplierDAO dao = new SupplierDAO();

        if (supplier == null) {
            // Thêm mới
            Supplier newSupplier = new Supplier();
            newSupplier.setName(name);
            newSupplier.setPhone(phone);
            newSupplier.setEmail(email);
            newSupplier.setAddress(address);
            dao.insertSupplier(newSupplier);
        } else {
            // Cập nhật
            supplier.setName(name);
            supplier.setPhone(phone);
            supplier.setEmail(email);
            supplier.setAddress(address);
            dao.updateSupplier(supplier);
        }

        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    private boolean validateForm() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhà cung cấp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            phoneField.requestFocus();
            return false;
        }

        // Kiểm tra số điện thoại hợp lệ (ví dụ: chỉ chứa số và 9-11 chữ số)
        if (!phone.matches("\\d{9,11}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            phoneField.requestFocus();
            return false;
        }

        // Email không bắt buộc, nhưng nếu có thì phải đúng định dạng
        if (!email.isEmpty() && !email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return false;
        }

        // Địa chỉ không bắt buộc nhưng bạn có thể kiểm tra độ dài nếu cần
        if (!address.isEmpty() && address.length() < 5) {
            JOptionPane.showMessageDialog(this, "Địa chỉ quá ngắn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            addressField.requestFocus();
            return false;
        }

        return true;
    }

}
