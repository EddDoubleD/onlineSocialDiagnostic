package ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

@SuppressLint("ParcelCreator")
public class Question implements Parcelable {
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {

        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    private String text;
    private List<String> type;
    private boolean isImageQuestion;

    public Question(Parcel in) {
        String[] data = new String[3];
        in.readStringArray(data);
        text = data[0];
        isImageQuestion = Boolean.parseBoolean(data[2]);
    }

    public Question() {

    }

    public Question(String text, List<String> type, boolean isImageQuestion) {
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

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public boolean isImageQuestion() {
        return isImageQuestion;
    }

    public void setImageQuestion(boolean imageQuestion) {
        isImageQuestion = imageQuestion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{text, type.toString(), isImageQuestion ? "true" : "false"});
    }
}
