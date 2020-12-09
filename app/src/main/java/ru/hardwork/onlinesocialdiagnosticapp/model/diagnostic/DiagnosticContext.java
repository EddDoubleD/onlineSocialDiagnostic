package ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class DiagnosticContext implements Parcelable {


    private int current = 0;
    private DiagnosticTest diagnostic;

    public DiagnosticContext(Parcel in) {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public DiagnosticTest getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(DiagnosticTest diagnostic) {
        this.diagnostic = diagnostic;
    }
}
