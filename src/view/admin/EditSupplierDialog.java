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
}
