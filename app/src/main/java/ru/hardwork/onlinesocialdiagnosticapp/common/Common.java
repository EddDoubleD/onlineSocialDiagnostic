package ru.hardwork.onlinesocialdiagnosticapp.common;

import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Category;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.DiagnosticTest;
import ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic.Question;
import ru.hardwork.onlinesocialdiagnosticapp.model.user.User;

public class Common {
    public static long diagnosticId;
    public static int descPosition;
    public static User currentUser;
    // Пользователь из firebase
    public static FirebaseUser firebaseUser;

    public static List<Category> categoryList = new ArrayList<>();
    public static List<DiagnosticTest> diagnosticTests = new ArrayList<>();
    public static Set<Long> diagnosticTestsIds = new HashSet<>();
    public static List<Question> questions = new ArrayList<>();

    public static int[] colors;

    public static SQLiteDatabase database;

}
