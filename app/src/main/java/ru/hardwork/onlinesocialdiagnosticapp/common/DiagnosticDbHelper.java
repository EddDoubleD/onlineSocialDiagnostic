package ru.hardwork.onlinesocialdiagnosticapp.common;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DiagnosticDbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION  = 1;
    private static final String DB_NAME = "SocialDiagnostic.db";

    public DiagnosticDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DiagnosticDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DiagnosticDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UIDataUtils.CREATE_DB_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(UIDataUtils.CREATE_DB_QUERY);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
