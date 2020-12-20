package ru.hardwork.onlinesocialdiagnosticapp.application;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.common.Common;
import ru.hardwork.onlinesocialdiagnosticapp.common.DataManager;
import ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticDbHelper;

public class OnlineSocialDiagnosticApp extends Application {

    protected static OnlineSocialDiagnosticApp _instance;
    private DiagnosticDbHelper helper;
    private DataManager dataManager;

    public static OnlineSocialDiagnosticApp getInstance() {
        return _instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        // инициализация цветов
        colorsInit();
    }

    public SQLiteOpenHelper getDbHelper() {
        if (helper == null) {
            helper = new DiagnosticDbHelper(getBaseContext());
        }
        return helper;
    }

    public DataManager getDataManager() {
        if (dataManager == null) {
            dataManager = new DataManager(getResources());
        }
        return dataManager;
    }

    private void colorsInit() {
        Common.shapes = new int[]{
                R.drawable.purple_shape,
                R.drawable.peach_shape,
                R.drawable.mustard_shape,
                R.drawable.round_two,
                R.drawable.pale_purple__shape
        };
    }
}
