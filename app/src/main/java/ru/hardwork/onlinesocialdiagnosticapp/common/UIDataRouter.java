package ru.hardwork.onlinesocialdiagnosticapp.common;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import ru.hardwork.onlinesocialdiagnosticapp.R;

/**
 * Класс-маршрутизатор по локальному контенту приложения
 */
public class UIDataRouter {

    public static final String USER_NAME = "USER_NAME";
    public static final String DEFAULT_USER = "guest";
    public static final String DIAGNOSTIC_ID = "DIAGNOSTIC_ID";

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
    /**
     * Методика исследования самоотношения
     */
    private static final int query_self_attitude_research = R.raw.query_self_attitude_research;

    public static ImmutableMap<Integer, Integer> questions = ImmutableMap.<Integer, Integer>builder()
            .put(0, question_default) // По умолчанию
            .put(1, question_leonhard_shmishek) // Опросник Шмишека
            .put(2, question_eysenck) // Личностный опросник Айзенка
            .put(3, question_aggression_level) // Опросник исследования уровня агрессивности
            .put(4, question_manifestations_anxiety) // Личностная шкала проявлений тревоги
            .put(5, query_self_attitude_research) // Методика исследования самоотношения
            .build();

    public static int getResourceOrDefault(int id) {
        return Optional.fromNullable(questions.get(id)).or(question_default);
    }


    //endregion
}
