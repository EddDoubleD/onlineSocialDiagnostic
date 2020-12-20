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
    /**
     * Диагностика эмоционального выгорания
     */
    private static final int query_burnout_diagnostics = R.raw.question_burnout_diagnostics;
    /**
     * Диагностика состояния стресса
     */
    private static final int query_stress_diagnostics = R.raw.question_stress_diagnostics;
    /**
     * Шкала невроза
     */
    private static final int query_scale_of_neurosis = R.raw.question_scale_of_neurosis;
    /**
     * Диагностика мотивации к достижению успеха
     */
    private static final int query_motivation_to_achieve_success = R.raw.query_motivation_to_achieve_success;
    /**
     * Методика изучения коммуникативных и организаторских способностей
     */
    private static final int query_study_of_communication_skills = R.raw.question_study_of_communication_skills;
    /**
     * Методика исследование уровня эмпатийных тенденций
     */
    private static final int question_self_attitude_research = R.raw.question_self_attitude_research;

    public static ImmutableMap<Integer, Integer> questions = ImmutableMap.<Integer, Integer>builder()
            .put(0, question_default) // По умолчанию
            .put(1, question_leonhard_shmishek) // Опросник Шмишека
            .put(2, question_eysenck) // Личностный опросник Айзенка
            .put(3, question_aggression_level) // Опросник исследования уровня агрессивности
            .put(4, question_manifestations_anxiety) // Личностная шкала проявлений тревоги
            .put(5, query_self_attitude_research) // Методика исследования самоотношения
            .put(6, query_burnout_diagnostics) // Диагностика эмоционального выгорания
            .put(7, query_stress_diagnostics) // Диагностика состояния стресса
            .put(8, query_scale_of_neurosis) // Шкала невроза
            .put(9, query_motivation_to_achieve_success) // Диагностика мотивации к достижению успеха
            .put(10, query_study_of_communication_skills) // Методика изучения коммуникативных и организаторских способностей

            .put(12, question_self_attitude_research)
            .build();

    public static int getResourceOrDefault(int id) {
        return Optional.fromNullable(questions.get(id)).or(question_default);
    }


    //endregion
}
