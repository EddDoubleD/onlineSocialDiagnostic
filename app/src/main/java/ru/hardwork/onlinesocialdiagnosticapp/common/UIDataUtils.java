package ru.hardwork.onlinesocialdiagnosticapp.common;

import android.content.res.Resources;

import com.google.android.gms.common.util.CollectionUtils;

import java.util.Arrays;

import ru.hardwork.onlinesocialdiagnosticapp.R;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Category;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;

public class UIDataUtils {


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
