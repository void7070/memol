package com.example.memoi.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class Task {

    // primary key for the Instance
    public String created;  // instanceNum NOT NULL, not displayed for user

    // in general case, this can't be null
    public String title;            // title NOT NULL

    public String description;

    /*LocalDate*/ String date;
    /*LocalTime*/ String time;


    public static final class NullIntegrityException extends RuntimeException {
        NullIntegrityException() {
            super("title cannot be null");
        }
    }

    Task(String title, String description, String date, String time) {
        this.created = LocalDateTime.now().toString();

        // parsable String checking
        if (date != null) LocalDate.parse(date);
        if (time != null) LocalTime.parse(time);

        if (title == null) throw new Task.NullIntegrityException();

        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    Task(String title, String description, LocalDate date, LocalTime time) {
        if (title == null) throw new Task.NullIntegrityException();

        this.created = LocalDateTime.now().toString();
        this.title = title;
        this.description = description;
        this.date = date == null ? null : date.toString();
        this.time = time == null ? null : time.toString();
    }


    // get/set -ters
    public void setTitle(String t) { this.title = t; }
    public void setDescription(String d) { this.description = d; }
}
