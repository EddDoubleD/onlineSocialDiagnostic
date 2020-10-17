package ru.hardwork.onlinesocialdiagnosticapp.common;

import android.content.res.Resources;

import java.util.Arrays;
import java.util.List;

import ru.hardwork.onlinesocialdiagnosticapp.Model.Decryption;
import ru.hardwork.onlinesocialdiagnosticapp.R;

public class DataManager {

    private Resources resources;
    private List<Decryption> decryption;

    private JSONResourceReader reader;

    public DataManager(Resources resources) {
        this.resources = resources;
        reader = new JSONResourceReader(getResources(), R.raw.decryption);
        decryption = Arrays.asList(reader.constructUsingGson(Decryption[].class));
    }

    public List<Decryption> getDecryption() {
        return decryption;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }
}
