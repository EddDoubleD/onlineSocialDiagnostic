package ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic;

public class DiagnosticTest {

    private long diagnosticTestId;
    private String name;
    private String description;
    private long categoryId;
    private String questionCount;
    private String testDuration;
    private String metricId;

    public DiagnosticTest() {

    }

    public DiagnosticTest(long diagnosticTestId, String name, String description, long categoryId, String questionCount, String testDuration, String metricId) {
        this.diagnosticTestId = diagnosticTestId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.questionCount = questionCount;
        this.testDuration = testDuration;
        this.metricId = metricId;
    }


    public long getDiagnosticTestId() {
        return diagnosticTestId;
    }

    public void setDiagnosticTestId(long diagnosticTestId) {
        this.diagnosticTestId = diagnosticTestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(String questionCount) {
        this.questionCount = questionCount;
    }

    public String getTestDuration() {
        return testDuration;
    }

    public void setTestDuration(String testDuration) {
        this.testDuration = testDuration;
    }

    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }
}
