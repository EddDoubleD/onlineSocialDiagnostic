package ru.hardwork.onlinesocialdiagnosticapp.common.lite;

import android.provider.BaseColumns;

import static ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract.DiagnosticEntry.DATE_PASSED;
import static ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract.DiagnosticEntry.DIAGNOSTIC_ID;
import static ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract.DiagnosticEntry.EMAIL;
import static ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract.DiagnosticEntry.RESULT;
import static ru.hardwork.onlinesocialdiagnosticapp.common.lite.DiagnosticContract.DiagnosticEntry.RESULT_ID;

/**
 * Контракт для БД, todo оставить только кэш
 */
public final class DiagnosticContract {
    public static final String CREATE_DB_QUERY = "CREATE TABLE IF NOT EXISTS users_results(" + RESULT_ID + " INTEGER PRIMARY KEY," +
            EMAIL + " TEXT NOT NULL," +
            DIAGNOSTIC_ID + " INTEGER NOT NULL," +
            RESULT + " TEXT NOT NULL," +
            DATE_PASSED + " TEXT NOT NULL);"; // только текстовое представление даты


    private DiagnosticContract() {

    }

    public static class DiagnosticEntry implements BaseColumns {
        /**
         * Таблица рещультаты пользователей
         */
        public static final String RESULT_TABLE = "users_results";
        /**
         * Идентификатор результата
         */
        public static final String RESULT_ID = "result_id";
        /**
         *
         */
        public static final String EMAIL = "email";
        public static final String DIAGNOSTIC_ID = "diagnosticId";
        public static final String RESULT = "result";
        public static final String DATE_PASSED = "date_passed";

        public static final String USERS_TABLE = "users";
        public static final String UNFINISHED_TABLE = "unfinished";


    }
}
