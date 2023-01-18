package com.example.memoi.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import kotlin.jvm.JvmName;

public class Todo extends Task {

    // TODO: find appropriate class for location
    String url;

    Todo(String title, String description, String date, String time, String url) {
        super(title, description, date, time);
        this.url = url;
    }

    Todo(String title, String description, LocalDate date, LocalTime time, String url) {
        super(title, description, date, time);
        this.url = url;
    }


    /* formatting to
     * "{title}, {description}, {time}, {date}, {url}\n"
     */
    public String toCsvFormat() {

        String res = "";
        //res += instanceNum + ", ";
        res += title + ", ";
        res += (description == null ? "null" : description) + ", ";

        res += (date == null ? "null" : date) + ", ";
        res += (time == null ? "null" : time) + ", ";
        res += (url == null ? "null" : url);

        System.out.println(res);
        return res + "\n";
    }

    // get/set -ters

      // for loading from firestore.
    public void setCreated(String c) {
        LocalDateTime.parse(c);
        this.created = c;
    }

    public void setTitle(String t) { this.title = t; }
    public void setDescription(String d) { this.description = d; }


    public String getDate() { return this.date; }

    public String getTime() { return this.time; }

    public LocalDate getLocalDate() { return this.date == null ? null :
            LocalDate.parse(this.date); }

    public LocalTime getLocalTime() { return this.time == null ? null :
            LocalTime.parse(this.time); }

    @JvmName(name = "TodoSetDate")
    public void setNewDate(LocalDate date) {
        this.date = date.toString();
    }

    @JvmName(name = "TodoSetDate")
    public void setNewDate(String date) {
        LocalDate.parse(date);  // parsable checking
        this.date = date;
    }

    @JvmName(name = "TodoSetDate")
    public void setNewDate(int y, int m, int d) {
        this.setNewDate(LocalDate.of(y, m, d));
    }

    public void setNewTime(LocalTime time) {
        this.time = time.toString();
    }

    public void setNewTime(String time) {
        LocalTime.parse(time);
        this.time = time;
    }

    public void setTime(int h, int m) {
        this.setNewTime(LocalTime.of(h, m));
    }

    public String getUrl() { return this.url; }

    // get unique Id (Integer) of todo
    // based on created field
    public int getUniqueId() {
        LocalDateTime tmp = LocalDateTime.parse(created);

        String resStr = "" +
                (tmp.getDayOfYear() * (tmp.getYear() - 2000)) +
                (tmp.getHour() * 24 * 60 + tmp.getMinute() * 60 + tmp.getSecond());
        return Integer.parseInt(resStr);
    }

    public Todo deepCopy() {
        return new Todo(title, description, date, time ,url);
    }

    // toString() Override
    @Override
    public String toString() {
        return "<Object Todo : " + created + ">" +
                "\nTitle: " + title +
                "\nDescription: " + description +
                "\nDate: " + date +
                "\nTime: " + time +
                "\nUrl: " + url +
                "\n";
    }
}
