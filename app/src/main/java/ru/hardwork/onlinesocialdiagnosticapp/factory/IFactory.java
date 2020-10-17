package ru.hardwork.onlinesocialdiagnosticapp.factory;

import java.util.List;

/**
 * Фабрика строитель элементов повторяющегося интерфейса
 *
 * @param <T> ViewModel который будет соответствовать layout
 */
public interface IFactory<T> {

    /**
     * Строитель
     *
     * @return лист построенных ViewModel
     */
    List<T> build();
}
