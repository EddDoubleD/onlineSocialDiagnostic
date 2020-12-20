package ru.hardwork.onlinesocialdiagnosticapp.factory.strategy;

import java.util.List;

import ru.hardwork.onlinesocialdiagnosticapp.factory.DescriptionViewModel;

/**
 * Стратегия обработки результата для использования в {@link ru.hardwork.onlinesocialdiagnosticapp.factory.DecryptionViewModelFactory}
 */
public interface DecryptionStrategy {

    /**
     * @return формирование моделей формирования результатов
     */
    List<DescriptionViewModel> form();
}
