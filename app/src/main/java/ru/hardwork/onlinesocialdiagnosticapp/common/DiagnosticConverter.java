package ru.hardwork.onlinesocialdiagnosticapp.common;

import com.google.common.base.Function;

import java.util.HashMap;

import ru.hardwork.onlinesocialdiagnosticapp.Model.DiagnosticTest;

public class DiagnosticConverter implements Function<Object, DiagnosticTest> {

    @org.checkerframework.checker.nullness.qual.Nullable
    @Override
    public DiagnosticTest apply(@org.checkerframework.checker.nullness.qual.Nullable Object input) {
        DiagnosticTest diagnostic = new DiagnosticTest();
        if (input instanceof HashMap) {
            HashMap map = (HashMap) ((HashMap) input).values().iterator().next();
            diagnostic.setDiagnosticTestId((long) map.get("diagnosticTestId"));
            diagnostic.setCategoryId((long) map.get("categoryId"));
            diagnostic.setName((String) map.get("name"));
            diagnostic.setDescription((String) map.get("description"));
            diagnostic.setMetricId((String) map.get("metricId"));
            return diagnostic;
        }

        return diagnostic;
    }
}
