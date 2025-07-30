package controller;

import model.User;
import dao.UserDAO;
import view.LoginForm;
import view.admin.AdminMainForm;

import java.awt.event.ActionListener;
import view.staff.StaffMainForm;

public class LoginController {
    private LoginForm view;
    private UserDAO userDAO;

    public LoginController(LoginForm view) {
        this.view = view;
        this.userDAO = new UserDAO();
        view.addLoginListener(e -> login());
    }

    private void login() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            view.showMessage("Vui lòng nhập tên đăng nhập và mật khẩu");
            return;
        }

        User user = userDAO.findByUsernameAndPassword(username, password);
        if (user != null) {
            view.dispose();
            if ("admin".equals(user.getRole())) {
                new AdminMainForm().setVisible(true);
            } else if ("staff".equals(user.getRole())) {
                new StaffMainForm().setVisible(true);
            }
        } else {
            view.showMessage("Tên đăng nhập hoặc mật khẩu không đúng");
        }
    }
}