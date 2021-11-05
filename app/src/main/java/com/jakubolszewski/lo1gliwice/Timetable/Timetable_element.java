package com.jakubolszewski.lo1gliwice.Timetable;

public class Timetable_element {
    public String hours;
    public String room;
    public String lesson;

    public Timetable_element(String hours, String lesson, String room) {
        this.hours = hours;
        this.room = room;
        this.lesson = lesson;
    }
}
