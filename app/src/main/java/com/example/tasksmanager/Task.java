package com.example.tasksmanager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {
    private String title;
    private String description;
    private String location;
    private Date due;

    public Task(String title, String description, String location, Date due) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.due = due;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
        String formattedDueDate = (due != null) ? dateFormat.format(due) : "No due date";
        return " " + title + "\n Due: " + formattedDueDate;
    }
}

