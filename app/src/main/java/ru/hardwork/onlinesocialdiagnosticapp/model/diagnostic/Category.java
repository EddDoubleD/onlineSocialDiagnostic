package ru.hardwork.onlinesocialdiagnosticapp.model.diagnostic;

public class Category {

    private long categoryId;
    private String name; // category name
    private int count;

    public Category() {

    }

    public Category(long categoryId, String name, int count) {
        this.categoryId = categoryId;
        this.name = name;
        this.count = count;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
