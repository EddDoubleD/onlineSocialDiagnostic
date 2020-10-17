package ru.hardwork.onlinesocialdiagnosticapp.factory;

/**
 * Модель для описания layout_description
 */
public class DescriptionViewModel {
    /**
     * Наименование метрики
     */
    private String name;
    /**
     * Максимальное значение шкалы
     */
    private int max;
    /**
     * Текущее значение шкалы
     */
    private int current;

    public DescriptionViewModel() {

    }

    public DescriptionViewModel(String name, int max, int current) {
        this.name = name;
        this.max = max;
        this.current = current;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
