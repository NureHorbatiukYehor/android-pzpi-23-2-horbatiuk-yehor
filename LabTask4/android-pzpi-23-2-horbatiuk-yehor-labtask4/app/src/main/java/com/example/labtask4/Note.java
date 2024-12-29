package com.example.labtask4;

public class Note {
    private String title;
    private String description;
    private int priority;
    private String dateTime;
    private String imagePath;

    public Note(String title, String description, int priority, String dateTime, String imagePath) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dateTime = dateTime;
        this.imagePath = imagePath;
    }

    // Геттери та сеттери
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return title.equals(note.title) &&
                description.equals(note.description) &&
                priority == note.priority &&
                dateTime.equals(note.dateTime);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + priority;
        result = 31 * result + dateTime.hashCode();
        return result;
    }
}

