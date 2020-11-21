package ru.hardwork.onlinesocialdiagnosticapp.model.firebase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * diagnostic invitation
 */
public class Invite {
    /**
     * unique id
     */
    private String id;
    /**
     * unique user id
     */
    private String auth;
    /**
     * link to recommended diagnostics
     */
    private int diagnosticId;
    /**
     * Alias
     */
    private String alias;
    /**
     * date of creation
     */
    private Date createDate;
    /**
     * diagnostic results by invitation
     */
    private List<Result> results;

    /**
     * empty public constructor
     */
    public Invite() {

    }

    /**
     * Should be used to insert
     */
    public Invite(String id, String auth, int diagnosticId, String alias, Date createDate) {
        this.id = id;
        this.auth = auth;
        this.diagnosticId = diagnosticId;
        this.alias = alias;
        this.createDate = createDate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void addResult(Result result) {
        if (this.results == null) {
            this.results = new ArrayList<>();
        }
        this.results.add(result);
    }

    public List<Result> getResults() {
        return this.results;
    }

    public int getDiagnosticId() {
        return diagnosticId;
    }

    public void setDiagnosticId(int diagnosticId) {
        this.diagnosticId = diagnosticId;
    }

    /**
     * diagnostic result by invitation
     */
    public static class Result {
        /**
         * diagnostic answers
         */
        private String answers;
        /**
         * date of diagnosis
         */
        private Date passedDate;

        public Result() {

        }

        public Result(String answers, Date passedDate) {
            this.answers = answers;
            this.passedDate = passedDate;
        }

        public String getAnswers() {
            return answers;
        }

        public void setAnswers(String answers) {
            this.answers = answers;
        }

        public Date getPassedDate() {
            return passedDate;
        }

        public void setPassedDate(Date passedDate) {
            this.passedDate = passedDate;
        }
    }
}
