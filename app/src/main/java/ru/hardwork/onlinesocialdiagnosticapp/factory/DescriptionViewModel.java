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
     * Описание
     */
    private String description;
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

    public DescriptionViewModel(String name, String description, int max, int current) {
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
