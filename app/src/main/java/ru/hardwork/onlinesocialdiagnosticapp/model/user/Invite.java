package ru.hardwork.onlinesocialdiagnosticapp.model.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Invite {
    private String guid;
    private String author;
    private String alias;
    private int diagnosticId;

    private List<Result> results = new ArrayList<>();

    public Invite() {

    }

    public Invite(String guid, String author, String alias, int diagnosticId) {
        this.guid = guid;
        this.author = author;
        this.alias = alias;
        this.diagnosticId = diagnosticId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getDiagnosticId() {
        return diagnosticId;
    }

    public void setDiagnosticId(int diagnosticId) {
        this.diagnosticId = diagnosticId;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public static class Result {
        private Date date;
        private String data;

        public Result(Date date, String data) {
            this.date = date;
            this.data = data;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
