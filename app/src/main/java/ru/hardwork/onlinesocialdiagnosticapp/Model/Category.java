package ru.hardwork.onlinesocialdiagnosticapp.Model;

public class Category {

    private long categoryId;
    private String Name; // category name
    private int count;

    public Category() {

    }

    public Category(long categoryId, String name, int count) {
        this.categoryId = categoryId;
        this.Name = name;
        this.count = count;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
