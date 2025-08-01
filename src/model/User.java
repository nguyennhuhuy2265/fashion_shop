package model;

public class User {

    private int id;
    private String username;
    private String password;
    private String role;
    private boolean isActive;
    private String fullname;

    public User(int id, String username, String password, String role, boolean isActive, String fullname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.fullname = fullname;
    }

    public User() {
        // constructor mặc định
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getFullname() {
        return fullname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
