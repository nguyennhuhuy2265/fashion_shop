package view;



import view.LoginForm;
import controller.LoginController;

public class Main {
    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm();
        new LoginController(loginForm);
        loginForm.setVisible(true);
    }
}