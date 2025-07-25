//package controller;
//
//import dao.UserDAO;
//import model.User;
//import view.admin.StaffForm;
//
//import javax.swing.*;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
//import java.util.List;
//
//public class UserController {
//
//    private final StaffForm staffForm;
//    private final UserDAO userDAO;
//    private User selectedUser;
//
//    public UserController(StaffForm staffForm) {
//        this.staffForm = staffForm;
//        this.userDAO = new UserDAO();
//        this.selectedUser = null;
//
//        init();
//    }
//
//    private void init() {
//        loadStaffUsers();
//        initListeners();
//    }
//
//    private void loadStaffUsers() {
//        List<User> staffList = userDAO.getAllStaffUsers();
//        staffForm.loadUserList(staffList);
//    }
//
//    private void initListeners() {
//        // Lắng nghe chọn dòng trong bảng
//        staffForm.getStaffTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent e) {
//                int selectedRow = staffForm.getStaffTable().getSelectedRow();
//                if (selectedRow >= 0) {
//                    int userId = (int) staffForm.getStaffTable().getValueAt(selectedRow, 0);
//                    selectedUser = userDAO.getUserById(userId);
//                    if (selectedUser != null) {
//                        staffForm.setUserDetail(selectedUser);
//                        staffForm.getEditButton().setText("Cập nhật");
//                    }
//                }
//            }
//        });
//
//        // Khi nhấn "Thêm nhân viên" – chỉ chuẩn bị thêm
//        staffForm.getAddButton().addActionListener(e -> {
//            staffForm.clearInputFields();                     // Xoá trắng form
//            staffForm.getStaffTable().clearSelection();       // Bỏ chọn dòng
//            selectedUser = null;                              // Không chọn ai cả
//            staffForm.getEditButton().setText("Thêm");  
//            System.out.println("Bạn đã ấn nút Thêm");  // Kiểm tra sự kiện// Đổi nút cập nhật thành "Thêm"
//        });
//
//        // Khi nhấn nút "Cập nhật" hoặc "Thêm"
//        staffForm.getEditButton().addActionListener(e -> {
//            User inputUser = staffForm.getUserFromInput();
//
//            if (inputUser.getUsername().trim().isEmpty()
//                    || inputUser.getPassword().trim().isEmpty()
//                    || inputUser.getFullname().trim().isEmpty()) {
//                JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin.");
//                return;
//            }
//
//            inputUser.setRole("staff");
//
//            String action = staffForm.getEditButton().getText();
//            if (action.equalsIgnoreCase("Thêm")) {
//                // Thêm mới
//                if (userDAO.insertUser(inputUser)) {
//                    JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công.");
//                } else {
//                    JOptionPane.showMessageDialog(null, "Tên đăng nhập đã tồn tại.");
//                    return;
//                }
//            } else {
//                // Cập nhật
//                if (selectedUser == null) {
//                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhân viên để cập nhật.");
//                    return;
//                }
//
//                inputUser.setId(selectedUser.getId());
//
//                if (userDAO.updateUser(inputUser)) {
//                    JOptionPane.showMessageDialog(null, "Cập nhật thành công.");
//                } else {
//                    JOptionPane.showMessageDialog(null, "Cập nhật thất bại.");
//                    return;
//                }
//            }
//
//            // Làm mới giao diện sau khi xử lý
//            loadStaffUsers();
//            staffForm.clearInputFields();
//            staffForm.getStaffTable().clearSelection();
//            selectedUser = null;
//            staffForm.getEditButton().setText("Cập nhật"); // Reset nút về "Cập nhật"
//        });
//    }
//}
