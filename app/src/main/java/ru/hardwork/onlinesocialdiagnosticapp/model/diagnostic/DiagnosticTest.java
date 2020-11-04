package ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic;

public class DiagnosticTest {

    private int id;
    private String name;
    private String description;
    private String fullDescription;
    private long categoryId;
    private String questionCount;
    private String testDuration;
    private int metricId;

    public DiagnosticTest() {

    }

    public DiagnosticTest(int id, String name, String description, String fullDescription, long categoryId, String questionCount, String testDuration, int metricId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.fullDescription = fullDescription;
        this.categoryId = categoryId;
        this.questionCount = questionCount;
        this.testDuration = testDuration;
        this.metricId = metricId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getMetricId() {
        return metricId;
    }

    public void setMetricId(int metricId) {
        this.metricId = metricId;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
}
