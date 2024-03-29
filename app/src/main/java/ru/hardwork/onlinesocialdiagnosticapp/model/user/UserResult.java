package ru.hardwork.onlinesocialdiagnosticapp.model.user;

import java.util.Date;

/**
 * Результаты/пользователи
 */
public class UserResult {
    private String id;
    private String user;
    private int diagnosticId;
    private String result;
    private Date date;

    public UserResult() {

    }

    public UserResult(String id, String user, int diagnosticId, String result, Date date) {
        this.id = id;
        this.user = user;
        this.diagnosticId = diagnosticId;
        this.result = result;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getDiagnosticId() {
        return diagnosticId;
    }

    public void setDiagnosticId(int diagnosticId) {
        this.diagnosticId = diagnosticId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
