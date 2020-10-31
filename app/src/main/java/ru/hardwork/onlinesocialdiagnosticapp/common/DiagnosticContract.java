package ru.hardwork.onlinesocialdiagnosticapp.common;

import android.provider.BaseColumns;

public final class DiagnosticContract {
    private DiagnosticContract() {

    }

    public static class DiagnosticEntry implements BaseColumns {
        public static final String RESULT_TABLE = "result";
        public static final String UNFINISHED_TABLE = "unfinished";
        public static final String USERS_TABLE = "users";


    }
}
