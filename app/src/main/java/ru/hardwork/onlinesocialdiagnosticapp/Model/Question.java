package ru.hardwork.onlinesocialdiagnosticapp.Model;

public class Question {
    private String text;
    private String type;
    private boolean isImageQuestion;

    public Question() {

    }

    public Question(String text, String type, boolean isImageQuestion) {
        this.text = text;
        this.type = type;
        this.isImageQuestion = isImageQuestion;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isImageQuestion() {
        return isImageQuestion;
    }

    public void setImageQuestion(boolean imageQuestion) {
        isImageQuestion = imageQuestion;
    }

    public int getCost(String question) {
        if (type.equalsIgnoreCase("yn")) {
            return question.equalsIgnoreCase("да") ? 1 : 0;
        }

        return 0;
    }

}
