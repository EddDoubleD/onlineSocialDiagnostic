package ru.hardwork.onlinesocialdiagnosticapp.common;

import android.content.res.Resources;

import com.google.android.gms.common.util.CollectionUtils;

import java.util.Arrays;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Category;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;

public class UIDataUtils {
    //todo вынести в scripts
    public static final String CREATE_DB_QUERY = "CREATE TABLE IF NOT EXISTS result( result_id integer PRIMARY KEY, email text NOT NULL, diagnosticId integer NOT NULL, result text NOT NULL, date_passed date NOT NULL); CREATE TABLE IF NOT EXISTS unfinished ( diagnosticId integer NOT NULL UNIQUE, result text NOT NULL, email text NOT NULL ); CREATE TABLE IF NOT EXISTS users ( user_id integer PRIMARY KEY, email text NOT NULL, identifier text);";

    public static void init(Resources resources) {
        Common.categoryList.clear();
        JSONResourceReader categoryReader = new JSONResourceReader(resources, R.raw.category);
        if (CollectionUtils.isEmpty(Common.categoryList)) {
            Common.categoryList.addAll(Arrays.asList(categoryReader.constructUsingGson(Category[].class)));
        }
        Common.diagnosticTests.clear();
        JSONResourceReader diagnosticReader = new JSONResourceReader(resources, R.raw.diagnostic_test);
        if (CollectionUtils.isEmpty(Common.diagnosticTests)) {
            Common.diagnosticTests.addAll(Arrays.asList(diagnosticReader.constructUsingGson(DiagnosticTest[].class)));
        }
    }
}
