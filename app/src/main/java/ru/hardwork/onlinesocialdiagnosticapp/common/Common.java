package ru.hardwork.onlinesocialdiagnosticapp.common;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Category;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Question;

public class Common {
    // Пользователь из firebase
    public static FirebaseUser firebaseUser;
    //
    public static List<Category> categoryList = new ArrayList<>();
    public static List<DiagnosticTest> diagnosticTests = new ArrayList<>();
    // Вопросы
    public static List<Question> questions = new ArrayList<>();
    // Массив
    public static int[] shapes;
}
