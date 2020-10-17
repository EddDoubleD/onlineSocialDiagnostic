package ru.hardwork.onlinesocialdiagnosticapp.Model;

public class User {
    private String logIn;
    private String password;
    private String email;

    public User() {

    }

    public User(String logIn, String password, String email) {
        this.logIn = logIn;
        this.password = password;
        this.email = email;
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
}
