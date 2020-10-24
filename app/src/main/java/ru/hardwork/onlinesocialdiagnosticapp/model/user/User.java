package ru.hardwork.onlinesocialdiagnosticapp.model.user;

public class User {
    private String logIn;
    private String password;
    private String email;
    private Role role;

    public User() {

    }

    public User(String logIn, String password, String email, String role) {
        this.logIn = logIn;
        this.password = password;
        this.email = email;
        this.role = Role.valueOf(role);
    }

    public String getLogIn() {
        return logIn;
    }

    public void setLogIn(String logIn) {
        this.logIn = logIn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public enum Role {
        GUEST, USER, SPECIALIST
    }
}
