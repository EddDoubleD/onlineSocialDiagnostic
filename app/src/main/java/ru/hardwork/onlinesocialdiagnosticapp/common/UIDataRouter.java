package ru.hardwork.onlinesocialdiagnosticapp.common;

import com.google.common.collect.ImmutableMap;

import ru.hardwork.onlinesocialdiagnosticapp.R;

/**
 * Класс-маршрутизатор по локальному контенту приложения
 */
public class UIDataRouter {

    //region questions
    /**
     * Вопросы по-умолчанию
     */
    private static final int question_default = R.raw.question_default;
    /**
     * Вопросы для опросника Шмишека
     */
    private static final int question_leonhard_shmishek = R.raw.question_leonhard_shmishek;
    /**
     * Вопросы для опросника Айзенка
     */
    private static final int question_eysenck = R.raw.question_eysenck;
    /**
     * Вопросы опросника исследования уровня агрессивности
     */
    private static final int question_aggression_level = R.raw.question_aggression_level;
    /**
     * Вопросы для шкала проявлений тревоги
     */
    private static final int question_manifestations_anxiety = R.raw.question_manifestations_anxiety;

    public static ImmutableMap<Integer, Integer> questions = ImmutableMap.<Integer, Integer>builder()
            .put(0, question_default)
            .put(1, question_leonhard_shmishek)
            .put(2, question_eysenck)
            .put(3, question_aggression_level)
            .put(4, question_manifestations_anxiety)
            .build();

    public static boolean containsQuestion(int id) {
        return questions.containsKey(id);
    }


    //endregion
}
