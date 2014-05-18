package ru.anisart.intechtest;

public class DataModel {
    private int id;
    private String title;
    private String description;
    private String img;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
